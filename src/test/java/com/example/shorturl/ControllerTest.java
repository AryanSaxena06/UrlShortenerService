package com.example.shorturl;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.server.reactive.ServerHttpResponse;

import com.example.shorturl.controller.ReactiveUrlShorteningController;
import com.example.shorturl.model.UrlDto;
import com.example.shorturl.model.UrlReport;
import com.example.shorturl.service.ReactiveUrlService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class ControllerTest {

	@Mock
	private ReactiveUrlService service;
	
	@InjectMocks
	private ReactiveUrlShorteningController controller;
	
	ServerHttpResponse response;
	
	@Test
	public void generateTest()
	{
		UrlDto urlDto =new UrlDto("Aryan","");
		String mockShortUrl ="http://localhost:8080/miniurl.com/a7f73864";
		
		Mockito.when(service.generateShortUrl(urlDto)).thenReturn(Mono.just(mockShortUrl));
		
		Mono<String> result = controller.generateShortUrl(urlDto,response);
		StepVerifier
		.create(result)
		.expectNext(mockShortUrl)
		.verifyComplete(); 
	}
	
	@Test
	public void getTest()
	{
		//UrlDto urlDto =new UrlDto("Aryan","");
		String ShortUrl ="a7f73864";
		//String mockFullShortUrl ="http://localhost:8080/miniurl.com/a7f73864";
		String mockLongUrl  = "https://www.digitalocean.com/community/tutorials/spring-configuration-annotation";
		
		Mockito.when(service.Redirect(ShortUrl)).thenReturn(Mono.just(mockLongUrl));
		
		Mono<Void> result = controller.redirectToLongUrl(ShortUrl,response);
		
		StepVerifier
		.create(result)
		.expectComplete(); 
	}
	
	@Test
	public void getUrlByCreationDate()
	{
		String ShortUrl ="a7f73864";
		LocalDate date =LocalDate.now();
		UrlReport urlReport =new UrlReport("1",ShortUrl,date,date,0);
		
		Mockito.when(service.getAllByDate(date)).thenReturn(Flux.just(urlReport));
		
		Flux<UrlReport> result = controller.getAllByDate(date,response);
		
		StepVerifier
		.create(result)
		.expectNext(urlReport)
		.expectComplete()
		.verify();
	}
	
	@Test
	public void UrlByHits()
	{
		String ShortUrl ="a7f73864";
		LocalDate date =LocalDate.now();
		UrlReport urlReport =new UrlReport("1",ShortUrl,date,date,1);
		
		Mockito.when(service.getAllUrlByHits(date)).thenReturn(Flux.just(urlReport));
		
		Flux<UrlReport> result = controller.getAllByHits(date,response);
		
		StepVerifier
		.create(result)
		.expectNext(urlReport)
		.expectComplete()
		.verify();
	}
	
}
