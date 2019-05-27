package com.scifar.aiops.importer.executor;

import java.util.Map;

import org.neo4j.driver.v1.AuthToken;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BoltCypherExecutor implements CypherExecutor {
	protected static final Logger logger = LoggerFactory.getLogger(BoltCypherExecutor.class);

	private final Driver driver;
	
	public BoltCypherExecutor(String uri) {
		this(uri,null,null);
	}
	
	public BoltCypherExecutor(String uri, String username, String password) {
		boolean hasPassword = (password != null && !password.trim().isEmpty());
		AuthToken authToken = hasPassword ? AuthTokens.basic(username, password) : AuthTokens.none();
		
		if(logger.isDebugEnabled()) {
			logger.debug("*********** DB URL: {} Username {}",uri, username);
		}
		driver = GraphDatabase.driver(uri,authToken,Config.build().withEncryption().toConfig());
	} 
	
	@Override
	public StatementResult query(String statement, Map<String, Object> params) {
		try (Session session = driver.session()) {
			StatementResult result = session.run(statement, params);
			return result;
		}
	}

	@Override
	public StatementResult query(String statement) {
		try (Session session = driver.session()) {
			StatementResult result = session.run(statement);
			return result;
		}
	}
	
	static Object convert(Value value) {
		switch (value.type().name()) {
		case "PATH":
			return value.asList(BoltCypherExecutor::convert);
		case "NODE":
		case "RELATIONSHIP":
			return value.asMap();
		}
		return value.asObject();
	}

	@Override
	public void close() throws Exception {
		if(driver != null) {
			driver.close();
		}
	}
	
	/*
	 * public static void main(String[] args) {
	 * logger.info("Getting Neo4j Connection"); BoltCypherExecutor cypher = new
	 * BoltCypherExecutor("bolt://54.241.75.85:7687","neo4j","admin");
	 * logger.info("Neo4j Connection Completed");
	 * 
	 * }
	 */
}
