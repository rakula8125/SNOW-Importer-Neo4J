package com.scifar.aiops.importer.snow;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.scifar.aiops.importer.executor.BoltCypherExecutor;
import com.scifar.aiops.importer.executor.CypherExecutor;
import com.scifar.aiops.importer.util.ConfigurationUtil;

@Component
public abstract class ImporterSourceClient implements AutoCloseable {
	protected static final Logger logger = LoggerFactory.getLogger(ImporterSourceClient.class);

	@Autowired
	ConfigurationUtil configUtil;

	protected CypherExecutor cypher;

	@PostConstruct 
	public void createCypherExecutor() { 
		if(logger.isDebugEnabled()) {
			logger.debug(" ############## createCypherExecutor ############## ");
		}
		cypher = new BoltCypherExecutor(configUtil.getNeo4jUrl(),configUtil.getNeo4jUsername(),
				configUtil.getNeo4jPassword()); 
	}
}
