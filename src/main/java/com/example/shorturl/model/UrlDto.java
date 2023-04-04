package com.example.shorturl.model;
//user give this bean while Post Mapping
public class UrlDto 
{
	private String userid;
	private String url;
	
	public UrlDto(String userid, String url) {
		super();
		this.userid = userid;
		this.url = url;
	}
	public UrlDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "UrlDto [userid=" + userid + ", url=" + url + "]";
	}
	
	
	
}
