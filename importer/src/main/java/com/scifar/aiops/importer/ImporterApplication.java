package com.scifar.aiops.importer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan("{com.scifar.aiops.importer.*}")
public class ImporterApplication {
	private static Logger logger = LoggerFactory.getLogger(ImporterApplication.class);
	
	public static void main(String[] args) {
		//logger.info("STARTING THE IMPORTER");
		
		ApplicationContext ctx = SpringApplication.run(ImporterApplication.class, args);
		try {
			Importer importer = ctx.getBean(Importer.class);
			System.exit(importer.run());
		} catch (Throwable t1) {
			throw t1;
		} 
		
		//logger.info("IMPORTER FINISHED");
		
		/*
		 * @Override public void run(String... args) {
		 * LOG.info("EXECUTING : command line runner"); for (int i = 0; i < args.length;
		 * ++i) { LOG.info("args[{}]: {}", i, args[i]); } }
		 */
		
		/*
		 * @PostConstruct 
		 * public void init () { options = new Options(); parser = new
		 * DefaultParser(); formatter = new HelpFormatter();
		 * 
		 * final Option fileOption = Option.builder("f") .argName("file") .hasArg()
		 * .desc("File to be analyzed") .build();
		 * 
		 * options.addOption(fileOption); }
		 */
	}

}
