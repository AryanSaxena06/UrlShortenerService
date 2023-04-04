package com.example.shorturl.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.example.shorturl.model.Url;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ReactiveUrlRepository extends  ReactiveMongoRepository<Url, Long>{

	Mono<Url> findByShortUrl(String shortUrl);
	 Mono<Url> findByLongUrl(String longUrl);
	 Flux<Url> findByUserid(String userid);
	 Mono<Url>findByLongUrlAndUserid(String longUrl, String userid);
}
