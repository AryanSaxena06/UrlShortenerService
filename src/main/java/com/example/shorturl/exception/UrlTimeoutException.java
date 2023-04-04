package com.example.shorturl.exception;


public class UrlTimeoutException extends RuntimeException
{
		String message;
		
		public UrlTimeoutException(String message)
		{
			super(message);
		}
	
}
