package com.scifar.aiops.importer.snow;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
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
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scifar.aiops.importer.exception.ImporterUpdateException;
import com.scifar.aiops.importer.exception.SourceInitialisationException;
import com.scifar.aiops.importer.snow.model.incident.Incident;

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

	protected String httpPostResponse(String url) throws ImporterUpdateException {
		String responseBody = null;

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

			responseBody = EntityUtils.toString(response.getEntity());
			if(logger.isDebugEnabled()) {
				logger.debug("Response Body: {}",responseBody);
			}

		} catch (IOException e) {
			throw new ImporterUpdateException("IOException: " + e.getMessage());
		} 
		return responseBody;
	}

	protected String httpGetResponse(String url) throws ImporterUpdateException {
		String responseBody = null;

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

			responseBody = EntityUtils.toString(response.getEntity());
			if(logger.isDebugEnabled()) {
				logger.debug("Response Body: {}",responseBody);
			}
		} catch (IOException e) {
			throw new ImporterUpdateException("IOException: " + e.getMessage());
		} 
		return responseBody;
	}


	public void importData() throws SourceInitialisationException, ImporterUpdateException {
		logger.info("Loading and merge process started for Service Now");
		Date startDate = new Date();

		//Merging Nodes
		updateIncidents();
		updateChangeRequests();
		
		Date endDate = new Date();
		this.initTimeMs = endDate.getTime() - startDate.getTime();
		logger.info(String.format("Data Updated in %.3f seconds", 0.001 * (double)this.initTimeMs));
	}


	private void updateIncidents() throws ImporterUpdateException {
		logger.info("-------------- Processing Incidents - START --------------");
		if(logger.isDebugEnabled()) {
			logger.debug(" ############## Loading Incidents############## ");
		}
		String incidents = httpGetResponse("https://"+configUtil.getSBaseURL()+"/"+configUtil.getSIncidentURL());
		ObjectMapper mapper = new ObjectMapper();

		if(logger.isDebugEnabled()) {
			logger.debug(" ############## Loading Incidents complete ############## ");
		}
		Map mappedIncidents = null;
		try {
			mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
			if(logger.isDebugEnabled()) {
				logger.debug(" ############## Incidents - Mapping JSON to Java Object ############## ");
			}	
			//Incident result = mapper.readValue(incidents,Incident.class);
			mappedIncidents = mapper.readValue(incidents,Map.class);

			if(logger.isDebugEnabled()) { 
				if(mappedIncidents != null) {
					logger.debug(" ############## Completed mapping values for Incidents size {}",mappedIncidents.size()); 
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
			//StatementResult result = cypher.query(query,Collections.singletonMap("result", mappedIncidents));
			StatementResult result = cypher.query(importIncidentsQuery,mappedIncidents);
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
		String changeRequests = httpGetResponse("https://"+configUtil.getSBaseURL()+"/"+configUtil.getSChangeRequestURL());
		ObjectMapper mapper = new ObjectMapper();

		if(logger.isDebugEnabled()) {
			logger.debug(" ############## Loading change_requests complete ############## ");
		}
		Map mappedChangeRequests = null;
		try {
			mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
			if(logger.isDebugEnabled()) {
				logger.debug(" ############## Incidents - Mapping JSON to Java Object ############## ");
			}	
			//Incident result = mapper.readValue(incidents,Incident.class);
			mappedChangeRequests = mapper.readValue(changeRequests,Map.class);

			if(logger.isDebugEnabled()) { 
				if(mappedChangeRequests != null) {
					logger.debug("############## Completed mapping values for Change Requests size {}",mappedChangeRequests.size()); 
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
			StatementResult result = cypher.query(importChangeRequestsQuery,mappedChangeRequests);
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


	private void mergeServers() {
		logger.debug("Loading Servers");

	}

	private void mergeConfigurationItems() {
		logger.debug("Loading CI");

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
