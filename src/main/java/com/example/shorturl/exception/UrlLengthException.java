package com.example.shorturl.exception;


public class UrlLengthException extends RuntimeException {

	String message;
	
	public UrlLengthException(String message)
	{
		super (message);
	}
}
