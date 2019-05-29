package com.scifar.aiops.importer;

import java.util.Date;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.scifar.aiops.importer.exception.ImporterUpdateException;
import com.scifar.aiops.importer.exception.SourceInitialisationException;
import com.scifar.aiops.importer.snow.SnowClient;

import ch.qos.logback.classic.Logger;

@Component()
public class Importer extends ImporterRunnerBase {
	private static Logger logger = (Logger) LoggerFactory.getLogger(Importer.class);
	
	int status = 0;
	
	@Autowired
	@Qualifier("snowClient")
	private SnowClient sClient;
	
	public int run() {
		printLogFilesInfo(logger);
		Date startDate = new Date();
		logger.info("Import Started {}",startDate);
		
		try {
			sClient.init();
			sClient.importData();
		} catch (SourceInitialisationException e) {
			this.logError(String.format("SourceInitialisationException while reading the Data: %s",e.getMessage()));
			if(logger.isDebugEnabled()) {
				e.printStackTrace();
			}
			return 1;
		} catch (ImporterUpdateException e) {
			this.logError(String.format("ImporterUpdateException while updating the Data: %s",e.getMessage()));
			if(logger.isDebugEnabled()) {
				e.printStackTrace();
			}
			return 1;
		}
		
		logger.info("Import Completed");
		
		
		return status;
	}
	
	private void logError(String errorMessage) {
		logger.error(errorMessage);
	}
}
