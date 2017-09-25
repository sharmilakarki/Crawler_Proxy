package com.sharmila.scrapper.domain;

import java.util.Date;

public class JobSummary {
   
	private String title; 
	private String jobType;
	private String locations;
	private String companyName;
	private String jobPostDate;
	private String link;
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLocations() {
		return locations;
	}
	public void setLocations(String locations) {
		this.locations = locations;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getJobPostDate() {
		return jobPostDate;
	}
	public void setJobPostDate(String jobPostDate) {
		this.jobPostDate = jobPostDate;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	@Override
	public String toString() {
		return "JobSummary [title=" + title + ", jobType=" + jobType + ", locations=" + locations + ", companyName="
				+ companyName + ", jobPostDate=" + jobPostDate + ", link=" + link + "]";
	}
	
	
	
	
}
