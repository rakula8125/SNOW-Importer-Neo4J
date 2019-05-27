package com.scifar.aiops.importer.exception;

public class ImporterUpdateException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2078107577409740621L;

	public ImporterUpdateException(String message) {
		super(message);
	}
	
	public ImporterUpdateException(String message, Throwable cause) {
		super(message,cause);
	}

}
