package com.example.shorturl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.shorturl.model.ErrorDetails;

@ControllerAdvice
public class CustomizeResponseEntityHandler 
{
	@ExceptionHandler(ArguementNotValidException.class)
	public ResponseEntity<ErrorDetails> notValidArguement(Exception ex)
	{
		ErrorDetails error =new ErrorDetails();
		error.setErrorDetails("your Given Url is not Valid");
		error.setMessage(ex.getMessage());
		
		return new ResponseEntity<ErrorDetails>(error,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(UrlNotFoundException.class)
	public ResponseEntity<ErrorDetails> urlNotFound(Exception ex)
	{
		ErrorDetails error=new ErrorDetails();
		error.setErrorDetails("your short url is incorrect");
		error.setMessage(ex.getMessage());
		
		return new ResponseEntity<ErrorDetails>(error,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(UrlTimeoutException.class)
	public ResponseEntity<ErrorDetails> urlTimeOut(Exception ex)
	{
		ErrorDetails error=new ErrorDetails();
		error.setErrorDetails("url no longer exixt");
		error.setMessage(ex.getMessage());
		
		return new ResponseEntity<ErrorDetails>(error,HttpStatus.GATEWAY_TIMEOUT);
	}
	
	@ExceptionHandler(UrlLengthException.class)
	public ResponseEntity<ErrorDetails> urlLenghtShort(Exception ex)
	{
		ErrorDetails error=new ErrorDetails();
		error.setErrorDetails("url too short ");
		error.setMessage(ex.getMessage());
		
		return new ResponseEntity<ErrorDetails>(error,HttpStatus.NOT_ACCEPTABLE);
	}
	
}
