package com.example.shorturl.exception;


public class ArguementNotValidException extends RuntimeException{
	String message;
	
	public ArguementNotValidException(String message)
	{
		super(message);
	}
	
	

}
