package com.scifar.aiops.importer.snow;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scifar.aiops.importer.exception.ImporterUpdateException;
import com.scifar.aiops.importer.exception.SourceInitialisationException;

@Service("snowClient")
public class SnowClient extends ImporterSourceClient {
	protected static final Logger logger = LoggerFactory.getLogger(SnowClient.class);

	private CloseableHttpClient httpClient = null;
	private String baseUrl;
	private String userName;
	private String password;

	private Map<String, Object> changeRequestsJson = null;
	private Map<String, Object> incidentsJson = null;
	private Map<String, Object> problemsJson = null;
	private Map<String, Object> cmdbCiRelsJson = null;


	private long initTimeMs;

	public void init() {
		logger.info("Initialising Source Client");
		this.baseUrl = configUtil.getSBaseURL();
		this.userName = configUtil.getSUsername();
		this.password = configUtil.getSPassword();
		initHttpClient();
	}


	private void initHttpClient() throws SourceInitialisationException {
		if(this.httpClient == null) {
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(
					new AuthScope(new HttpHost(baseUrl)),
					new UsernamePasswordCredentials(userName,password));

			this.httpClient = HttpClients.custom()
					.setDefaultCredentialsProvider(credsProvider)
					.build();
		}
	}

	protected InputStream httpPostResponse(String url) throws ImporterUpdateException {
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-Type", "application/json");
		logger.info("Executing request {}", httpPost.getRequestLine());
		try {
			CloseableHttpResponse response = httpClient.execute(httpPost);
			if(logger.isDebugEnabled()) {
				logger.debug("HTTP request complete: {}", url);
			}

			if(response == null) {
				throw new ImporterUpdateException("Null response from http client: " + url);
			}

			if(response.getStatusLine() == null) {
				throw new ImporterUpdateException("Null status line from http client: "+ url);
			}

			int statusCode = response.getStatusLine().getStatusCode();

			if(logger.isDebugEnabled()) {
				logger.debug("HTTP request complete: "+ statusCode +" " + url);
			}

			if(statusCode != HttpStatus.SC_OK) {
				throw new ImporterUpdateException("Status code "+ statusCode +"recieved from URL: " + url);
			}

			return response.getEntity().getContent();
		} catch (IOException e) {
			throw new ImporterUpdateException("IOException: " + e.getMessage());
		} 
	}

	protected InputStream httpGetResponse(String url) throws ImporterUpdateException {
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("Accept", "application/json");
		logger.info("Executing request {}", httpGet.getRequestLine());

		try {
			CloseableHttpResponse response = httpClient.execute(httpGet);

			if(logger.isDebugEnabled()) {
				logger.debug("HTTP request complete: {}", url);
			}

			if(response == null) {
				throw new ImporterUpdateException("Null response from http client: " + url);
			}

			if(response.getStatusLine() == null) {
				throw new ImporterUpdateException("Null status line from http client: "+ url);
			}

			int statusCode = response.getStatusLine().getStatusCode();

			if(logger.isDebugEnabled()) {
				logger.debug("HTTP request complete: "+ statusCode +" " + url);
			}

			if(statusCode != HttpStatus.SC_OK) { 
				throw new ImporterUpdateException("Status code "+ statusCode +"recieved from URL: " + url); 
			}

			return response.getEntity().getContent();
		} catch (IOException e) {
			throw new ImporterUpdateException("IOException: " + e.getMessage());
		} 
	}

	/**
	 * Update Nodes with Data from source, in this case Service NOW
	 * @throws SourceInitialisationException
	 * @throws ImporterUpdateException
	 */
	public void updateNodes() throws SourceInitialisationException, ImporterUpdateException {
		logger.info("Loading and merge process started for Service Now");
		Date startDate = new Date();

		//Merging Nodes
		updateIncidents();
		updateChangeRequests();
		updateCIServers();
		updateConfigurationItems();
		updateProblems();

		Date endDate = new Date();
		this.initTimeMs = endDate.getTime() - startDate.getTime();
		logger.info(String.format("Data Updated in %.3f seconds", 0.001 * (double)this.initTimeMs));
	}

	/**
	 * Create/Update relationship(s) between nodes
	 * @throws SourceInitialisationException
	 * @throws ImporterUpdateException
	 */
	public void updateLinks() throws SourceInitialisationException, ImporterUpdateException {
		logger.info("Link(s) update process started for Service Now");
		Date startDate = new Date();

		//Merge Links between Nodes
		linkServiceWithCI();
		linkIncidentWithParent();
		linkIncidentsWithProblem();
		linkIncidentsWithService();
		linkServiceWithProblem();
		linkIncidentWithChangeRequest();

		Date endDate = new Date();
		this.initTimeMs = endDate.getTime() - startDate.getTime();
		logger.info(String.format("Link(s) between nodes Updated in %.3f seconds", 0.001 * (double)this.initTimeMs));
	}


	private void updateIncidents() throws ImporterUpdateException {
		logger.info("-------------- Processing Incidents - START --------------");
		if(logger.isDebugEnabled()) {
			logger.debug(" ############## updateIncidents - Loading JSON for Incidents############## ");
		}
		InputStream incidents = httpGetResponse("https://"+configUtil.getSBaseURL()+"/"+configUtil.getSIncidentURL());
		ObjectMapper mapper = new ObjectMapper();

		if(logger.isDebugEnabled()) {
			logger.debug(" ############## Loading JSON for Incidents complete ############## ");
		}
		try {
			if(logger.isDebugEnabled()) {
				logger.debug(" ############## Incidents - Mapping JSON to Java Object ############## ");
			}	
			TypeReference<Map<String,Object>> typeRef = new TypeReference<Map<String,Object>>() {};
			incidentsJson = mapper.readValue(incidents,typeRef);

		} catch (JsonParseException e) {
			throw new ImporterUpdateException("JsonParseException: "+ e.getMessage()); 
		} catch (JsonMappingException e) {
			throw new ImporterUpdateException("JsonMappingException: "+ e.getMessage());
		} catch (IOException e) {
			throw new ImporterUpdateException("IOException: "+ e.getMessage());
		}

		if(logger.isDebugEnabled()) {
			logger.debug(" ############## Updating Incidents ############## ");
		}
		String importIncidentsQuery = configUtil.getImportIncidents();
		if(importIncidentsQuery != null) {
			StatementResult result = cypher.executeCypherQuery(importIncidentsQuery,Collections.singletonMap("json", incidentsJson));
			if(result != null) {
				List<Record> records = result.list();
				logger.info("Updating -------------- {} records",records.size());
				if(logger.isDebugEnabled()) {
					for(Record record : records) {
						logger.debug(" ############## Add / Modified Incident ==> {}",record.get("number"));
					}
				}
			} 
		}
		logger.info("--------------Processing Incidents - END --------------");
	}


	private void updateChangeRequests() throws ImporterUpdateException {
		logger.info("-------------- Processing Change Requests - START --------------");
		if(logger.isDebugEnabled()) {
			logger.debug(" ############## updateChangeRequests - Loading JSON for change_requests ############## ");
		}
		InputStream changeRequests = httpGetResponse("https://"+configUtil.getSBaseURL()+"/"+configUtil.getSChangeRequestURL());
		ObjectMapper mapper = new ObjectMapper();
		//DeserializationFeature.
		if(logger.isDebugEnabled()) {
			logger.debug(" ############## Loading JSON for change_requests complete ############## ");
		}
		try {
			if(logger.isDebugEnabled()) {
				logger.debug(" ############## Incidents - Mapping JSON to Java Object ############## ");
			}	
			TypeReference<Map<String,Object>> typeRef = new TypeReference<Map<String,Object>>() {};
			changeRequestsJson = mapper.readValue(changeRequests,typeRef);

		} catch (JsonParseException e) {
			throw new ImporterUpdateException("JsonParseException: "+ e.getMessage()); 
		} catch (JsonMappingException e) {
			throw new ImporterUpdateException("JsonMappingException: "+ e.getMessage());
		} catch (IOException e) {
			throw new ImporterUpdateException("IOException: "+ e.getMessage());
		}

		if(logger.isDebugEnabled()) {
			logger.debug(" ############## Updating Change Requests ############## ");
		}
		String importChangeRequestsQuery = configUtil.getImportChangeRequests();
		if(importChangeRequestsQuery != null) {
			StatementResult result = cypher.executeCypherQuery(importChangeRequestsQuery,Collections.singletonMap("json", changeRequestsJson));
			if(result != null) {
				List<Record> records = result.list();
				logger.info("Updating -------------- {} records",records.size());
				if(logger.isDebugEnabled()) {
					for(Record record : records) {
						logger.debug(" ############## Add / Modified Change Requests ==> {}",record.get("number"));
					}
				}
			} 
		}
		logger.info("-------------- Processing Change Requests - END --------------");
	}


	private void updateCIServers() throws ImporterUpdateException {
		logger.info("-------------- Processing CI Servers - START --------------");
		if(logger.isDebugEnabled()) {
			logger.debug(" ############## updateCIServers - Loading JSON for cmdb_ci_server ############## ");
		}
		InputStream servers = httpGetResponse("https://"+configUtil.getSBaseURL()+"/"+configUtil.getSCiServerURL());
		ObjectMapper mapper = new ObjectMapper();

		if(logger.isDebugEnabled()) {
			logger.debug(" ############## Loading JSON for cmdb_ci_server complete ############## ");
		}
		Map<String, Object> json = null;
		try {
			if(logger.isDebugEnabled()) {
				logger.debug(" ############## CI Servers - Mapping JSON to Java Object ############## ");
			}	
			TypeReference<Map<String,Object>> typeRef = new TypeReference<Map<String,Object>>() {};
			json = mapper.readValue(servers,typeRef);

		} catch (JsonParseException e) {
			throw new ImporterUpdateException("JsonParseException: "+ e.getMessage()); 
		} catch (JsonMappingException e) {
			throw new ImporterUpdateException("JsonMappingException: "+ e.getMessage());
		} catch (IOException e) {
			throw new ImporterUpdateException("IOException: "+ e.getMessage());
		}

		if(logger.isDebugEnabled()) {
			logger.debug(" ############## Updating Servers ############## ");
		}
		String importServersQuery = configUtil.getImportServers();
		if(importServersQuery != null) {
			StatementResult result = cypher.executeCypherQuery(importServersQuery,Collections.singletonMap("json", json));
			if(result != null) {
				List<Record> records = result.list();
				logger.info("Updating -------------- {} records",records.size());
				if(logger.isDebugEnabled()) {
					for(Record record : records) {
						logger.debug(" ############## Add / Modified Servers ==> {}",record.get("name"));
					}
				}
			} 
		}
		logger.info("-------------- Processing Servers - END --------------");
	}

	private void updateConfigurationItems() throws ImporterUpdateException {
		logger.info("-------------- Processing CI Services - START --------------");
		if(logger.isDebugEnabled()) {
			logger.debug(" ############## updateConfigurationItems - Loading JSON for cmdb_ci_service ############## ");
		}
		InputStream ciServices = httpGetResponse("https://"+configUtil.getSBaseURL()+"/"+configUtil.getSCiServiceURL());
		ObjectMapper mapper = new ObjectMapper();

		if(logger.isDebugEnabled()) {
			logger.debug(" ############## Loading JSON for cmdb_ci_service complete ############## ");
		}
		Map<String, Object> json = null;
		try {
			if(logger.isDebugEnabled()) {
				logger.debug(" ############## CI Services - Mapping JSON to Java Object ############## ");
			}	
			TypeReference<Map<String,Object>> typeRef = new TypeReference<Map<String,Object>>() {};
			json = mapper.readValue(ciServices,typeRef);

		} catch (JsonParseException e) {
			throw new ImporterUpdateException("JsonParseException: "+ e.getMessage()); 
		} catch (JsonMappingException e) {
			throw new ImporterUpdateException("JsonMappingException: "+ e.getMessage());
		} catch (IOException e) {
			throw new ImporterUpdateException("IOException: "+ e.getMessage());
		}

		if(logger.isDebugEnabled()) {
			logger.debug(" ############## Updating CI Services ############## ");
		}
		String importCiServicesQuery = configUtil.getImportCIService();
		if(importCiServicesQuery != null) {
			StatementResult result = cypher.executeCypherQuery(importCiServicesQuery,Collections.singletonMap("json", json));
			if(result != null) {
				List<Record> records = result.list();
				logger.info("Updating -------------- {} records",records.size());
				if(logger.isDebugEnabled()) {
					for(Record record : records) {
						logger.debug(" ############## Add / Modified CI Services ==> {}",record.get("name"));
					}
				}
			} 
		}
		logger.info("-------------- Processing CI Services - END --------------");

	}

	private void updateProblems() throws ImporterUpdateException {
		logger.info("-------------- Processing Problems - START --------------");
		if(logger.isDebugEnabled()) {
			logger.debug(" ############## updateProblems - Loading JSON for problem ############## ");
		}
		InputStream problems = httpGetResponse("https://"+configUtil.getSBaseURL()+"/"+configUtil.getSProblemURL());
		ObjectMapper mapper = new ObjectMapper();

		if(logger.isDebugEnabled()) {
			logger.debug(" ############## Loading JSON for problem complete ############## ");
		}

		try {
			if(logger.isDebugEnabled()) {
				logger.debug(" ############## Problems - Mapping JSON to Java Object ############## ");
			}	
			TypeReference<Map<String,Object>> typeRef = new TypeReference<Map<String,Object>>() {};
			problemsJson = mapper.readValue(problems,typeRef);

		} catch (JsonParseException e) {
			throw new ImporterUpdateException("JsonParseException: "+ e.getMessage()); 
		} catch (JsonMappingException e) {
			throw new ImporterUpdateException("JsonMappingException: "+ e.getMessage());
		} catch (IOException e) {
			throw new ImporterUpdateException("IOException: "+ e.getMessage());
		}

		if(logger.isDebugEnabled()) {
			logger.debug(" ############## Updating Problems ############## ");
		}
		String importProblemsQuery = configUtil.getImportProblems();
		if(importProblemsQuery != null) {
			StatementResult result = cypher.executeCypherQuery(importProblemsQuery,Collections.singletonMap("json", problemsJson));
			if(result != null) {
				List<Record> records = result.list();
				logger.info("Updating -------------- {} records",records.size());
				if(logger.isDebugEnabled()) {
					for(Record record : records) {
						logger.debug(" ############## Add / Modified Problems ==> {}",record.get("number"));
					}
				}
			} 
		}
		logger.info("-------------- Processing Problems - END --------------");

	}


	private void linkServiceWithCI() throws ImporterUpdateException {
		logger.info("-------------- Linking 'Service - RUNS_ON -> Server' - START --------------");
		if(logger.isDebugEnabled()) {
			logger.debug(" ############## linkServiceWithCI - Loading JSON for cmdb.ci.rel ############## ");
		}

		InputStream	cmdbCiRels = httpGetResponse("https://"+configUtil.getSBaseURL()+"/"+configUtil.getSCmdbRelCiURL());

		ObjectMapper mapper = new ObjectMapper();

		if(logger.isDebugEnabled()) {
			logger.debug(" ############## Loading JSON for cmdb.ci.rel complete ############## ");
		}

		try {
			if(logger.isDebugEnabled()) {
				logger.debug(" ############## linkServiceWithCI-RunsOn  Mapping JSON to Java Object ############## ");
			}	
			TypeReference<Map<String,Object>> typeRef = new TypeReference<Map<String,Object>>() {};
			cmdbCiRelsJson = mapper.readValue(cmdbCiRels,typeRef);

		} catch (JsonParseException e) {
			throw new ImporterUpdateException("JsonParseException: "+ e.getMessage()); 
		} catch (JsonMappingException e) {
			throw new ImporterUpdateException("JsonMappingException: "+ e.getMessage());
		} catch (IOException e) {
			throw new ImporterUpdateException("IOException: "+ e.getMessage());
		}

		if(logger.isDebugEnabled()) {
			logger.debug(" ############## Updating RunsOn Link with Service and CI ############## ");
		}
		String importRunsOnQuery = configUtil.getImportRunsOn();
		if(importRunsOnQuery != null) {
			StatementResult result = cypher.executeCypherQuery(importRunsOnQuery,Collections.singletonMap("json", cmdbCiRelsJson));
			if(result != null) {
				List<Record> records = result.list();
				logger.info("Updating -------------- {} records",records.size());
				if(logger.isDebugEnabled()) {
					for(Record record : records) {
						logger.debug(" ############## Updated Relationship between Service and CI, {} => {}",record.get("T.name"),record.get("S.name"));
					}
				}
			} 
		}
		logger.info("-------------- Linking 'Service - RUNS_ON -> Server' - END --------------");
	}


	private void linkIncidentWithParent() throws ImporterUpdateException {
		logger.info("-------------- Linking 'Incident - HAS_PARENT_INCIDENT -> Parent Incident' - START --------------");

		String linkHasParentIncidentQuery = configUtil.getLinkHasParentIncident();
		if(linkHasParentIncidentQuery != null) {
			StatementResult result = cypher.executeCypherQuery(linkHasParentIncidentQuery,Collections.singletonMap("json", incidentsJson));
			if(result != null) {
				List<Record> records = result.list();
				logger.info("Updating -------------- {} records",records.size());
				if(logger.isDebugEnabled()) {
					for(Record record : records) {
						logger.debug(" ############## Updated Relationship between Incident and Parent Incident, {} => {}",record.get("i.number"),record.get("pInc.number"));
					}
				}
			} 
		}
		logger.info("-------------- Linking 'Incident - HAS_PARENT_INCIDENT -> Parent Incident' - END --------------");
	}

	private void linkIncidentsWithProblem() throws ImporterUpdateException {
		logger.info("-------------- Linking 'Incident - CREATES_PROBLEM -> Problem' - START --------------");

		String linkCreatesProblemQuery = configUtil.getLinkCreatesProblem();
		if(linkCreatesProblemQuery != null) {
			StatementResult result = cypher.executeCypherQuery(linkCreatesProblemQuery,Collections.singletonMap("json", incidentsJson));
			if(result != null) {
				List<Record> records = result.list();
				logger.info("Updating -------------- {} records",records.size());
				if(logger.isDebugEnabled()) {
					for(Record record : records) {
						logger.debug(" ############## Updated Relationship between Incident and Problem, {} => {}",record.get("i.number"),record.get("p.number"));
					}
				}
			} 
		}
		logger.info("-------------- Linking 'Incident - CREATES_PROBLEM -> Problem' - END --------------");
	}

	private void linkIncidentsWithService() throws ImporterUpdateException {
		logger.info("-------------- Linking 'Incident - IMPACTS_SERVICE -> Service' - START --------------");

		String linkImpactsServiceQuery = configUtil.getLinkImpactsService();
		if(linkImpactsServiceQuery != null) {
			StatementResult result = cypher.executeCypherQuery(linkImpactsServiceQuery,Collections.singletonMap("json", incidentsJson));
			if(result != null) {
				List<Record> records = result.list();
				logger.info("Updating -------------- {} records",records.size());
				if(logger.isDebugEnabled()) {
					for(Record record : records) {
						logger.debug(" ############## Updated Relationship between Incident and Service, {} => {}",record.get("i.number"),record.get("t.name"));
					}
				}
			} 
		}
		logger.info("-------------- Linking 'Incident - IMPACTS_SERVICE -> Service' - END --------------");
	}


	private void linkServiceWithProblem() throws ImporterUpdateException {
		logger.info("-------------- Linking 'Incident - AFFECTS -> Problem' - START --------------");

		String linkAffectsQuery = configUtil.getLinkAffects();
		if(linkAffectsQuery != null) {
			StatementResult result = cypher.executeCypherQuery(linkAffectsQuery,Collections.singletonMap("json", incidentsJson));
			if(result != null) {
				List<Record> records = result.list();
				logger.info("Updating -------------- {} records",records.size());
				if(logger.isDebugEnabled()) {
					for(Record record : records) {
						logger.debug(" ############## Updated Relationship between Incident and Problem, {} => {}",record.get("i.number"),record.get("p.number"));
					}
				}
			} 
		}
		logger.info("-------------- Linking 'Incident - AFFECTS -> Problem' - END --------------");
	}

	private void linkIncidentWithChangeRequest() throws ImporterUpdateException {
		logger.info("-------------- Linking 'Service - CAUSED_BY -> Problem' - START --------------");

		String linkAffectsQuery = configUtil.getLinkAffects();
		if(linkAffectsQuery != null) {
			StatementResult result = cypher.executeCypherQuery(linkAffectsQuery,Collections.singletonMap("json", incidentsJson));
			if(result != null) {
				List<Record> records = result.list();
				logger.info("Updating -------------- {} records",records.size());
				if(logger.isDebugEnabled()) {
					for(Record record : records) {
						logger.debug(" ############## Updated Relationship between Incident and Change Request, {} => {}",record.get("i.number"),record.get("c.number"));
					}
				}
			} 
		}
		logger.info("-------------- Linking 'Service - CAUSED_BY -> Problem' - END --------------");
	}


	@Override
	public void close() throws Exception {
		if(httpClient != null) {
			try {
				httpClient.close();
			} catch (IOException ioe) {
				logger.warn("Failed to cleanly close HttpClient.",ioe);
			}
			httpClient = null;
		}
	}
}
