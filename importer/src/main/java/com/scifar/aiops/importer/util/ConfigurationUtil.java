package com.scifar.aiops.importer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;
import lombok.Setter;

@PropertySource("classpath:config.properties")
@Configuration
@Getter @Setter
public class ConfigurationUtil {
	protected static final Logger logger = LoggerFactory.getLogger(ConfigurationUtil.class);
	
	@Value("${neo4j.url}")
	private String neo4jUrl;
	
	@Value("${neo4j.username}")
	private String neo4jUsername;
	
	@Value("${neo4j.password}")
	private String neo4jPassword;
	
	
	@Value("${snow.instance}")
	private String sInstance;
	
	@Value("${snow.baseURL}")
	private String sBaseURL;
	
	@Value("${snow.username}")
	private String sUsername;
	
	@Value("${snow.password}")
	private String sPassword;
	
	@Value("${snow.incidentURL}")
	private String sIncidentURL;
	
	@Value("${snow.changeRequestURL}")
	private String sChangeRequestURL;
	
	@Value("${snow.ci.serverURL}")
	private String sCiServerURL;
	
	@Value("${snow.ci.serviceURL}")
	private String sCiServiceURL;
	
	@Value("${import.incidents}")
	private String importIncidents;
	
	@Value("${import.changeRequests}")
	private String importChangeRequests;

	
	@Value("${import.ci.servers}")
	private String importServers;
	
	@Value("${import.ci.service}")
	private String importCIService;

	
}
