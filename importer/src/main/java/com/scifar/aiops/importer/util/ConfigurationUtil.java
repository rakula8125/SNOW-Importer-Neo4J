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
	
	@Value("${snow.problemURL}")
	private String sProblemURL;
	@Value("${snow.cmdb.rel.ciURL}")	
	private String sCmdbRelCiURL;
	
	
	@Value("${import.incidents}")
	private String importIncidents;
	
	@Value("${import.changeRequests}")
	private String importChangeRequests;
	
	@Value("${import.ci.servers}")
	private String importServers;
	
	@Value("${import.ci.service}")
	private String importCIService;
	
	@Value("${import.problems}")
	private String importProblems;
	
	@Value("${link.runs.on}")
	private String importRunsOn;
	
	@Value("${link.has.parent.incident}")
	private String linkHasParentIncident;
	
	@Value("${link.creates.problem}")
	private String linkCreatesProblem;
	
	@Value("${link.impacts.service}")
	private String linkImpactsService;
	
	@Value("${link.affects}")
	private String linkAffects;
	
	@Value("{link.caused.by}")
	private String linkCausedBy;
	
}
