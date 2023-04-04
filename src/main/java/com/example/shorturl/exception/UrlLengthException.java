package com.example.shorturl.exception;


public class UrlLengthException extends Throwable {

	String message;
	
	public UrlLengthException(String message)
	{
		super (message);
	}
}
