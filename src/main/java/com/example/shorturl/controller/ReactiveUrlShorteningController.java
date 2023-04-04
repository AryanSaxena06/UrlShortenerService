package com.example.shorturl.controller;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shorturl.model.Url;
import com.example.shorturl.model.UrlDto;
import com.example.shorturl.model.UrlReport;
import com.example.shorturl.service.ReactiveUrlService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@RestController
public class ReactiveUrlShorteningController 
{

	@Autowired
	private ReactiveUrlService service;
	
	
	@PostMapping("/create")
	public Mono<String> generateShortUrl(@RequestBody UrlDto urlDto,ServerHttpResponse response)
	{
	return service.generateShortUrl(urlDto)
			 .onErrorResume(throwable -> {
	                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
	                DataBuffer dataBuffer = response.bufferFactory().wrap(throwable.getMessage().getBytes(StandardCharsets.UTF_8));
	                return response.writeWith(Mono.just(dataBuffer)).then(Mono.empty());
	            });	
			
	}
	
	
	@GetMapping("miniurl.com/{shortUrl}")
	public Mono<Void> redirectToLongUrl(@PathVariable String shortUrl,ServerHttpResponse response)
	{
		return service.Redirect(shortUrl)
				.flatMap(longUrl->{
					response.setStatusCode(HttpStatus.PERMANENT_REDIRECT);
					response.getHeaders().setLocation(URI.create(longUrl));
					
					return response.setComplete();
				})
				 .onErrorResume(throwable -> {
		                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		                DataBuffer dataBuffer = response.bufferFactory().wrap(throwable.getMessage().getBytes(StandardCharsets.UTF_8));
		                return response.writeWith(Mono.just(dataBuffer)).then(Mono.empty());
		            });				
	}
	
	
	@GetMapping("/getUrlByCreationDate")
	public Flux<UrlReport> getAllByDate(@RequestParam LocalDate date,ServerHttpResponse response)
	{
		return service.getAllByDate(date)
				.onErrorResume(throwable -> {
	                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
	                DataBuffer dataBuffer = response.bufferFactory().wrap(throwable.getMessage().getBytes(StandardCharsets.UTF_8));
	                return response.writeWith(Mono.just(dataBuffer)).then(Mono.empty());
	            });	
	}
	
	
	@GetMapping("/UrlByHits")
	public Flux<UrlReport> getAllByHits (@RequestParam LocalDate date,ServerHttpResponse response)
	{
		return service.getAllUrlByHits(date)
				.onErrorResume(throwable -> {
	                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
	                DataBuffer dataBuffer = response.bufferFactory().wrap(throwable.getMessage().getBytes(StandardCharsets.UTF_8));
	                return response.writeWith(Mono.just(dataBuffer)).then(Mono.empty());
	            });	
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
