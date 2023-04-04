package com.example.shorturl.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "urlReport")
public class UrlReport {
	@Id
	private String id;
	private String shortUrl;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate createDate;
	private long hits;
	//private String status;

	public UrlReport() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UrlReport(String id, String shortUrl, LocalDate date, LocalDate createDate, long hits) {
		super();
		this.id = id;
		this.shortUrl = shortUrl;
		this.date = date;
		this.createDate = createDate;
		this.hits = hits;
		//this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalDate getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	public long getHits() {
		return hits;
	}

	public void setHits(long hits) {
		this.hits = hits;
	}

	/*public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}*/

	@Override
	public String toString() {
		return "UrlReport [id=" + id + ", shortUrl=" + shortUrl + ", date=" + date + ", createDate=" + createDate
				+ ", hits=" + hits + "]";
	}
	
	

}
