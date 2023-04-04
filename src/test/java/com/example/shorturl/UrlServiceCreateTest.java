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
import com.example.shorturl.model.Url;
import com.example.shorturl.model.UrlDto;
import com.example.shorturl.model.UrlReport;
import com.example.shorturl.repository.ReactiveUrlRepository;
import com.example.shorturl.repository.UrlReportRepository;
import com.example.shorturl.service.ReactiveUrlService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class UrlServiceCreateTest extends ShorturlApplicationTests{

	@Mock
	private ReactiveUrlRepository urlRepo;
	
	@Mock
	private UrlReportRepository urlReportRepo;
	
	@InjectMocks
	private ReactiveUrlService service;
	
	@Test
	public void blankLongUrlTest()
	{
		UrlDto urlDto =new UrlDto("Aryan","");
		
		Mono<String> response =service.generateShortUrl(urlDto).doOnNext(System.out::println);
		
		StepVerifier
		.create(response)
		.verifyError(ArguementNotValidException.class);
	}
	
	@Test
	public void longUrlContainsMiniurl()
	{
		UrlDto urlDto =new UrlDto("Aryan","miniurl");
		
		Mono<String> response =service.generateShortUrl(urlDto).doOnNext(System.out::println);
		
		StepVerifier
		.create(response)
		.verifyError(ArguementNotValidException.class);
	}
	
	@Test
	public void longUrlAlreadyShort()
	{
		UrlDto urlDto =new UrlDto("Aryan","LongUrltooShort.com");
		
		Mono<String> response =service.generateShortUrl(urlDto).doOnNext(System.out::println);
		
		StepVerifier
		.create(response)
		.verifyError(UrlLengthException.class);
	}
	
	@Test
	public void WhenNotInUrlDbAndNotInReportDb() throws Exception
	{
		String domain ="http://localhost:8080/miniurl.com/";
		String mockLongUrl  = "https://www.digitalocean.com/community/tutorials/spring-configuration-annotation";
		UrlDto mockUrlDto = new UrlDto("Aryan",mockLongUrl);
		String mockShortUrl ="a7f73864";
		Url mockUrl =new Url("1","Aryan",mockLongUrl,mockShortUrl,LocalDateTime.now(),LocalDateTime.now()) ;
		String mockUrlResponse=" UserId = "+mockUrlDto.getUserid()+"\n"+ "ShortUrl = " +domain+mockShortUrl;
		UrlReport mockUrlReport =new UrlReport("1",mockShortUrl,LocalDate.now(),LocalDate.now(),0);
		
		Mockito.when(urlRepo.findByLongUrlAndUserid(mockLongUrl,"Aryan")).thenReturn(Mono.empty());
		Mockito.when(urlRepo.save(Mockito.any(Url.class))).thenReturn(Mono.just(mockUrl));
		Mockito.when(urlReportRepo.findByShortUrl(mockShortUrl)).thenReturn(Mono.empty());
		Mockito.when(urlReportRepo.save(Mockito.any(UrlReport.class))).thenReturn(Mono.just(mockUrlReport));
		
		Mono<String> response = service.generateShortUrl(mockUrlDto).doOnNext(System.out::println);
		
		StepVerifier
		.create(response)
		.expectNext(mockUrlResponse)
		.verifyComplete(); 
		
	}
	
	@Test
	public void WhenInUrlDbAndInReportDb() throws Exception
	{
		String domain ="http://localhost:8080/miniurl.com/";
		String mockLongUrl  = "https://www.digitalocean.com/community/tutorials/spring-configuration-annotation";
		UrlDto mockUrlDto = new UrlDto("Aryan",mockLongUrl);
		String mockShortUrl ="a7f73864";
		String mockUrlResponse=" UserId = "+mockUrlDto.getUserid()+"\n"+ "ShortUrl = " +domain+mockShortUrl;
		Url mockUrl =new Url("1","Aryan",mockLongUrl,mockShortUrl,LocalDateTime.now(),LocalDateTime.now()) ;
		UrlReport mockUrlReport =new UrlReport("1",mockShortUrl,LocalDate.now(),LocalDate.now(),0);
		
		Mockito.when(urlRepo.findByLongUrlAndUserid(mockLongUrl,"Aryan")).thenReturn(Mono.just(mockUrl));
		Mockito.when(urlRepo.save(Mockito.any(Url.class))).thenReturn(Mono.just(mockUrl));
		Mockito.when(urlReportRepo.findByShortUrl(mockShortUrl)).thenReturn(Mono.just(mockUrlReport));
		Mockito.when(urlReportRepo.save(Mockito.any(UrlReport.class))).thenReturn(Mono.just(mockUrlReport));
		
		Mono<String> response = service.generateShortUrl(mockUrlDto).doOnNext(System.out::println);
		
		StepVerifier
		.create(response)
		.expectNext(mockUrlResponse)
		.verifyComplete();
		
	}
	
	@Test
	public void getAllByDate()
	{
		LocalDate date =LocalDate.now();
		String ShortUrl ="a7f73864";
		UrlReport urlReport =new UrlReport("1",ShortUrl,date,date,1);
		
		Mockito.when(urlReportRepo.findByCreateDateAndDate(date, date)).thenReturn(Flux.just(urlReport));
		
		Flux<UrlReport> response = service.getAllByDate(date).doOnNext(System.out::println);
		
		StepVerifier
		.create(response)
		.expectNext(urlReport)
		.expectComplete()
		.verify();
	}
	
	@Test
	public void getAllUrlByHits()
	{
		LocalDate date =LocalDate.now();
		String ShortUrl ="a7f73864";
		UrlReport urlReport =new UrlReport("1",ShortUrl,date,date,1);
		
		Mockito.when(urlReportRepo.findByDate(date)).thenReturn(Flux.just(urlReport));
		
		Flux<UrlReport> response = service.getAllUrlByHits(date).doOnNext(System.out::println);
		
		StepVerifier
		.create(response)
		.expectNext(urlReport)
		.expectComplete()
		.verify();
	}
	

}
