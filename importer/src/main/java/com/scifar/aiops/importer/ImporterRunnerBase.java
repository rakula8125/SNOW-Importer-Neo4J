package com.scifar.aiops.importer;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.FileAppender;

public class ImporterRunnerBase {
	
	static void printLogFilesInfo(Logger logger) {
		Set<String> logFiles = new HashSet<String>();
		Iterator logListIterator = ((LoggerContext)LoggerFactory.getILoggerFactory()).getLoggerList().iterator();
		
		while (logListIterator.hasNext()) {
			Logger log = (Logger) logListIterator.next();
			Iterator index = log.iteratorForAppenders();
			
			while(index.hasNext()) {
				Object enumElement = index.next();
				if(enumElement instanceof FileAppender) {
					logFiles.add(((FileAppender)enumElement).getFile());
				}
			}
		}
		logListIterator = logFiles.iterator();
		
		while(logListIterator.hasNext()) {
			String logPath = (String) logListIterator.next();
			String logDirPath = (new File(logPath)).getParent();
			String absoluteLogDirPath;
			if(logDirPath != null) {
				absoluteLogDirPath = (new File(logDirPath)).getAbsolutePath();
			} else {
				absoluteLogDirPath = (new File(".").getAbsolutePath());
			}
			
			logger.info("Logs are stored in {}",absoluteLogDirPath);
			File logFile = new File(logPath);
			logger.info("Log file is {}",logFile.getName());
		}
	}
}
