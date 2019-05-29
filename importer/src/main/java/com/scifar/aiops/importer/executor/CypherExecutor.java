package com.scifar.aiops.importer.executor;

import java.util.Map;

import org.neo4j.driver.v1.StatementResult;

public interface CypherExecutor extends AutoCloseable {
	StatementResult executeCypherQuery(String statement, Map<String, Object> params);
	StatementResult executeCypherQuery(String statement);
}
