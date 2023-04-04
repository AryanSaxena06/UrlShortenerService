package com.example.shorturl.exception;


public class UrlNotFoundException extends RuntimeException
{
	String message;

	public UrlNotFoundException(String message)
	{
		super(message);
	}

}
