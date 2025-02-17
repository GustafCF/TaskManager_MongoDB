package com.br.task_mongoDB.service.exceptions;

public class DatabaseException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public DatabaseException(String msg) {
		super("Database error: " + msg);
	}
}
