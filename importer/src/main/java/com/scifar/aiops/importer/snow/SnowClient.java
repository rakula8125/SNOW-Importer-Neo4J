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
import org.apache.http.util.EntityUtils;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
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


	public void importData() throws SourceInitialisationException, ImporterUpdateException {
		logger.info("Loading and merge process started for Service Now");
		Date startDate = new Date();

		//Merging Nodes
		updateIncidents();
		updateChangeRequests();
		updateServers();
		updateConfigurationItems();

		Date endDate = new Date();
		this.initTimeMs = endDate.getTime() - startDate.getTime();
		logger.info(String.format("Data Updated in %.3f seconds", 0.001 * (double)this.initTimeMs));
	}


	private void updateIncidents() throws ImporterUpdateException {
		logger.info("-------------- Processing Incidents - START --------------");
		if(logger.isDebugEnabled()) {
			logger.debug(" ############## Loading Incidents############## ");
		}
		InputStream incidentJsonPayload = httpGetResponse("https://"+configUtil.getSBaseURL()+"/"+configUtil.getSIncidentURL());
		ObjectMapper mapper = new ObjectMapper();

		if(logger.isDebugEnabled()) {
			logger.debug(" ############## Loading Incidents complete ############## ");
		}
		Map<String, Object> json = null;
		try {
			if(logger.isDebugEnabled()) {
				logger.debug(" ############## Incidents - Mapping JSON to Java Object ############## ");
			}	
			json = mapper.readValue(incidentJsonPayload,Map.class);

			logger.info("Mapped Incidents: {}",json);
			if(logger.isDebugEnabled()) { 
				if(json != null) {
					logger.debug(" ############## Completed mapping values for Incidents size {}",json.size()); 
				} 
			}

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
			StatementResult result = cypher.executeCypherQuery(importIncidentsQuery,Collections.singletonMap("json", json));
			if(result != null) {
				List<Record> records = result.list();
				logger.info("Updating -------------- {} records",records.size());
				if(logger.isDebugEnabled()) {
					for(Record record : records) {
						logger.debug(" ############## Add / Modified Incident ==> {}",record.get("number"));
					}
				}
			} else {
				logger.info("-------------- No records to update for Incident --------------");
			}
		}
		logger.info("--------------Processing Incidents - END --------------");
	}


	private void updateChangeRequests() throws ImporterUpdateException {
		logger.info("-------------- Processing Change Requests - START --------------");
		if(logger.isDebugEnabled()) {
			logger.debug(" ############## Loading change_requests ############## ");
		}
		InputStream changeRequests = httpGetResponse("https://"+configUtil.getSBaseURL()+"/"+configUtil.getSChangeRequestURL());
		ObjectMapper mapper = new ObjectMapper();

		if(logger.isDebugEnabled()) {
			logger.debug(" ############## Loading change_requests complete ############## ");
		}
		Map json = null;
		try {
			if(logger.isDebugEnabled()) {
				logger.debug(" ############## Incidents - Mapping JSON to Java Object ############## ");
			}	
			json = mapper.readValue(changeRequests,Map.class);

			if(logger.isDebugEnabled()) { 
				if(json != null) {
					logger.debug("############## Completed mapping values for Change Requests size {}",json.size()); 
				} 
			}

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
			StatementResult result = cypher.executeCypherQuery(importChangeRequestsQuery,Collections.singletonMap("json", json));
			if(result != null) {
				List<Record> records = result.list();
				logger.info("Updating -------------- {} records",records.size());
				if(logger.isDebugEnabled()) {
					for(Record record : records) {
						logger.debug(" ############## Add / Modified Change Requests ==> {}",record.get("number"));
					}
				}
			} else {
				logger.info("-------------- No records to update for Change Requests --------------");
			}
		}
		logger.info("-------------- Processing Change Requests - END --------------");
	}


	private void updateServers() throws ImporterUpdateException {
		logger.info("-------------- Processing Servers - START --------------");
		if(logger.isDebugEnabled()) {
			logger.debug(" ############## Loading cmdb_ci_server ############## ");
		}
		InputStream servers = httpGetResponse("https://"+configUtil.getSBaseURL()+"/"+configUtil.getSCiServerURL());
		ObjectMapper mapper = new ObjectMapper();

		if(logger.isDebugEnabled()) {
			logger.debug(" ############## Loading cmdb_ci_server complete ############## ");
		}
		Map json = null;
		try {
			if(logger.isDebugEnabled()) {
				logger.debug(" ############## Servers - Mapping JSON to Java Object ############## ");
			}	
			json = mapper.readValue(servers,Map.class);

			if(logger.isDebugEnabled()) { 
				if(json != null) {
					logger.debug("############## Completed mapping values for Servers size {}",json.size()); 
				} 
			}

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
			} else {
				logger.info("-------------- No records to update for Servers --------------");
			}
		}
		logger.info("-------------- Processing Servers - END --------------");
	}

	private void updateConfigurationItems() throws ImporterUpdateException {
		logger.info("-------------- Processing CI Services - START --------------");
		if(logger.isDebugEnabled()) {
			logger.debug(" ############## Loading cmdb_ci_service ############## ");
		}
		InputStream ciServices = httpGetResponse("https://"+configUtil.getSBaseURL()+"/"+configUtil.getSCiServiceURL());
		ObjectMapper mapper = new ObjectMapper();

		if(logger.isDebugEnabled()) {
			logger.debug(" ############## Loading cmdb_ci_service complete ############## ");
		}
		Map json = null;
		try {
			if(logger.isDebugEnabled()) {
				logger.debug(" ############## CI Services - Mapping JSON to Java Object ############## ");
			}	
			json = mapper.readValue(ciServices,Map.class);

			if(logger.isDebugEnabled()) { 
				if(json != null) {
					logger.debug("############## Completed mapping values for CI Services size {}",json.size()); 
				} 
			}

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
			} else {
				logger.info("-------------- No records to update for CI Services --------------");
			}
		}
		logger.info("-------------- Processing CI Services - END --------------");

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
