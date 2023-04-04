package com.example.shorturl.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "url")
public class Url 
{
	
	@Id
	private String id;
	private String userid;
	private String longUrl;
	private String shortUrl;
	private LocalDateTime creationDate;
	private LocalDateTime expirationDate;
	public Url() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public Url(String id, String userid, String longUrl, String shortUrl, LocalDateTime creationDate,
			LocalDateTime expirationDate) {
		super();
		this.id = id;
		this.userid = userid;
		this.longUrl = longUrl;
		this.shortUrl = shortUrl;
		this.creationDate = creationDate;
		this.expirationDate = expirationDate;
	}


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getLongUrl() {
		return longUrl;
	}
	public void setLongUrl(String longUrl) {
		this.longUrl = longUrl;
	}
	public String getShortUrl() {
		return shortUrl;
	}
	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}
	public LocalDateTime getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}
	public LocalDateTime getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(LocalDateTime expirationDate) {
		this.expirationDate = expirationDate;
	}
	@Override
	public String toString() {
		return "Url [id=" + id + ", userid=" + userid + ", longUrl=" + longUrl + ", shortUrl=" + shortUrl
				+ ", creationDate=" + creationDate + ", expirationDate=" + expirationDate + "]";
	}
	
	
	
	
}
