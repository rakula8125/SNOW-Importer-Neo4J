package com.scifar.aiops.importer.executor;

import java.util.Map;

import org.neo4j.driver.v1.StatementResult;

public interface CypherExecutor extends AutoCloseable {
	StatementResult query(String statement, Map<String, Object> params);
	StatementResult query(String statement);
}
