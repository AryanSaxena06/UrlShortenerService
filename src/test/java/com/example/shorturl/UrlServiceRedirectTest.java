package com.example.shorturl;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.shorturl.exception.ArguementNotValidException;
import com.example.shorturl.exception.UrlLengthException;
import com.example.shorturl.exception.UrlNotFoundException;
import com.example.shorturl.exception.UrlTimeoutException;
import com.example.shorturl.model.Url;
import com.example.shorturl.model.UrlReport;
import com.example.shorturl.repository.ReactiveUrlRepository;
import com.example.shorturl.repository.UrlReportRepository;
import com.example.shorturl.service.ReactiveUrlService;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class UrlServiceRedirectTest {
	
	@InjectMocks
	private ReactiveUrlService service;
	@Mock
	private ReactiveUrlRepository urlRepo;
	
	@Mock
	private UrlReportRepository urlReportRepo;
	
	
	@Test
	public void shortUrlNotHavingMiniurl()
	{
	
		String mockShortUrl ="a7f7386";
		//String mockLongUrl  = "https://www.digitalocean.com/community/tutorials/spring-configuration-annotation";
		//Url mockUrl =new Url("1","Aryan",mockLongUrl,mockShortUrl,LocalDateTime.now(),LocalDateTime.now()) ;
		
		Mockito.when(urlRepo.findByShortUrl(mockShortUrl)).thenReturn((Mono.empty()));
				
		Mono<String> response =service.Redirect(mockShortUrl).doOnNext(System.out::println);
		
		StepVerifier
		.create(response)
		.verifyError(UrlLengthException.class);
	}
	
	@Test
	public void shortUrlNotInDb()
	{
	
		String mockShortUrl ="a7f73864";
		//String mockLongUrl  = "https://www.digitalocean.com/community/tutorials/spring-configuration-annotation";
		//Url mockUrl =new Url("1",mockLongUrl,mockShortUrl,LocalDateTime.now(),LocalDateTime.now()) ;
		
		Mockito.when(urlRepo.findByShortUrl(mockShortUrl)).thenReturn((Mono.empty()));
		
		Mono<String> response =service.Redirect(mockShortUrl).doOnNext(System.out::println);
		
		StepVerifier
		.create(response)
		.verifyError(UrlNotFoundException.class);
	}
	
	@Test
	public void urlExpired() {
		String mockShortUrl ="a7f73864";
		String mockLongUrl  = "https://www.digitalocean.com/community/tutorials/spring-configuration-annotation";
		Url mockUrl =new Url("1","Aryan",mockLongUrl,mockShortUrl,LocalDateTime.now(),LocalDateTime.now().minusSeconds(5));
		
		Mockito.when(urlRepo.findByShortUrl(mockShortUrl)).thenReturn(Mono.just(mockUrl));
		Mockito.when(urlRepo.delete(mockUrl)).thenReturn(Mono.empty());
		
		Mono<String> response =service.Redirect(mockShortUrl).doOnNext(System.out::println);
		
		StepVerifier
		.create(response)
		.verifyError(UrlTimeoutException.class);
	}
	
	@Test
	public void urlNotExpired() {
		String mockShortUrl ="a7f73864";
		String mockLongUrl  = "https://www.digitalocean.com/community/tutorials/spring-configuration-annotation";
		Url mockUrl =new Url("1","Aryan",mockLongUrl,mockShortUrl,LocalDateTime.now(),LocalDateTime.now().plusDays(1));
		UrlReport urlReport =new UrlReport("1",mockShortUrl,LocalDate.now(),LocalDate.now(),0);
		
		
		Mockito.when(urlRepo.findByShortUrl(mockShortUrl)).thenReturn(Mono.just(mockUrl));
		Mockito.when(urlReportRepo.findByShortUrlAndDate(mockShortUrl, LocalDate.now())).thenReturn(Mono.just(urlReport));
		Mockito.when(urlReportRepo.save(Mockito.any(UrlReport.class))).thenReturn(Mono.just(new UrlReport("1",mockShortUrl,LocalDate.now(),LocalDate.now(),1)));
		//Mockito.when(urlRepo.delete(mockUrl)).thenReturn(Mono.empty());
		
		Mono<String> response =service.Redirect(mockShortUrl).doOnNext(System.out::println);
		
		StepVerifier
		.create(response)
		.expectNext(mockLongUrl)
		.verifyComplete();
	}
}
