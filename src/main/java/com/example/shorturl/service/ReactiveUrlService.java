package com.example.shorturl.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shorturl.exception.ArguementNotValidException;
import com.example.shorturl.exception.UrlLengthException;
import com.example.shorturl.exception.UrlNotFoundException;
import com.example.shorturl.exception.UrlTimeoutException;
import com.example.shorturl.model.Url;
import com.example.shorturl.model.UrlDto;
import com.example.shorturl.model.UrlReport;
import com.example.shorturl.repository.ReactiveUrlRepository;
import com.example.shorturl.repository.UrlReportRepository;
import com.google.common.hash.Hashing;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ReactiveUrlService 	
{
	@Autowired
	private ReactiveUrlRepository repo;
	
	@Autowired
	private UrlReportRepository urlReportRepo;
	
	private  String domain = "http://localhost:8080/miniurl.com/";
	
	Logger logger = LoggerFactory.getLogger(ReactiveUrlService.class);
	
			/*       POST METHOD    */
	//function creates short url and save and return shortUrl & Userid in response
	public Mono<String> generateShortUrl(UrlDto urlDto) 
	{
		logger.info("Generate method invoked at service with object " +urlDto);
		return Mono.just(urlDto)
		
				.filter(urlD ->UrlValidator.getInstance().isValid(urlD.getUrl()) && !urlD.getUrl().contains("miniurl")  )
				.switchIfEmpty(Mono.error(new ArguementNotValidException("Please Provide Valid Long Url")))
				
				.filter(urlD-> urlD.getUrl().length()>50)
				.switchIfEmpty(Mono.error(new UrlLengthException("Enter a Long Url of length more than 50 characters")))
				
				.flatMap(urldto ->
				          repo.findByLongUrlAndUserid(urldto.getUrl(),urldto.getUserid())
					      .switchIfEmpty(createUrl(urldto)))
				
				.flatMap(url-> urlReportRepo.findByShortUrl(url.getShortUrl())
						.switchIfEmpty(createUrlReport(url))
								.then(Mono.just(" UserId = "+url.getUserid()+"\n"+ "ShortUrl = " +domain+url.getShortUrl())))
				
				.doOnNext(msg->logger.info("Url Object Created"+msg));		
	} 
    
	//Function will create Object of Url
	public Mono<Url> createUrl(UrlDto urlDto)
	{
		Url url =new Url(null,urlDto.getUserid(),urlDto.getUrl(),encodeUrl(urlDto.getUrl()),LocalDateTime.now(),
				LocalDateTime.now().plusDays(1));
		return repo.save(url);
	}
	
	
	//function encode the given long Url with concatination with current date&Time 
	private String encodeUrl(String url) {
		String encodeUrl= "";
		LocalDateTime time =LocalDateTime.now();
		encodeUrl =Hashing.murmur3_32_fixed().hashString(url.concat(time.toString()),StandardCharsets.UTF_8).toString();
		return encodeUrl;
	}
	
	
	//create object of UrlReport with shortUrl and creation date and other fields as default
		private Mono<UrlReport> createUrlReport(Url url) {
			return  urlReportRepo.save( new UrlReport(null,url.getShortUrl(),LocalDate.now(),
					url.getCreationDate().toLocalDate(),0)); 
		}


	
			/*       GET METHOD    */
	//Function redirects to Long Url against the given Short Url
	public Mono<String> Redirect(String shortUrl) 
	{
		return  Mono.just(shortUrl)
				.filter(shortU ->shortU.length()==8)
				.doOnNext(System.out::println)
				.switchIfEmpty(Mono.error(new UrlLengthException(" Short Url is less than 8 characters")))
				
				.flatMap(urlShort->getEncodedUrlFromDb(urlShort))
				.switchIfEmpty(Mono.error(new UrlNotFoundException("short Url not present in db")))
				
				.filter(url->url.getExpirationDate().isAfter(LocalDateTime.now()))
				.switchIfEmpty(deleteShortUrl(shortUrl)
						.then(Mono.error(new UrlTimeoutException("Url expired"))))
				
				.flatMap(url->urlReportRepo.findByShortUrlAndDate(shortUrl, LocalDate.now())
						.flatMap(urlReport->updateHits(urlReport))
							.switchIfEmpty(createNewUrlReport(url))
								.then(Mono.just(url.getLongUrl())));
				
	} 
	
	
	//return Object of Url against short url in Db
	public Mono<Url> getEncodedUrlFromDb (String urlShort)
	{
		return repo.findByShortUrl(urlShort);
	}
		
		
	//delete the url object from db if it is expired	
	public  Mono<Void> deleteShortUrl(String shortUrl) {
		return repo.findByShortUrl(shortUrl)
				.flatMap(url->repo.delete(url));
		}
	//if for a particular date their is a entry then increase the hits by 1 in the same UrlReport Object
	private Mono<UrlReport> updateHits(UrlReport urlReport) {
		urlReport.setHits(urlReport.getHits()+1);
		return urlReportRepo.save(urlReport);
	}

		
	// creates a new urlReport Object if url in not expired and their is no entry for a particular date
	private Mono<? extends UrlReport> createNewUrlReport(Url url) {
		
		 return  urlReportRepo.save( new UrlReport(null,url.getShortUrl(),LocalDate.now(),
				url.getCreationDate().toLocalDate(),1)); 
	}

   //get all urls with the given creation date
	public Flux<UrlReport> getAllByDate(LocalDate date) {
		return urlReportRepo.findByCreateDateAndDate(date,date);
	}

	//get all url with the given date and provided hits not zero
	public Flux<UrlReport> getAllUrlByHits(LocalDate date) {
		
		return urlReportRepo.findByDate(date)
				.filter(urlReport->urlReport.getHits()>0);
	}

}
