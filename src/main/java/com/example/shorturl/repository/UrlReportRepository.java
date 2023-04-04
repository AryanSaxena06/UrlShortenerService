package com.example.shorturl.repository;

import java.time.LocalDate;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.example.shorturl.model.UrlReport;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UrlReportRepository extends  ReactiveMongoRepository<UrlReport, String>{

	Flux<UrlReport> findByCreateDate(LocalDate date);
	Mono<UrlReport> findByShortUrlAndDate(String shortUrl, LocalDate date);
	Flux<UrlReport> findByDate(LocalDate date);
	Mono<UrlReport> findByShortUrl(String shortUrl);
	Flux<UrlReport> findByCreateDateAndDate(LocalDate date, LocalDate date2);
}
