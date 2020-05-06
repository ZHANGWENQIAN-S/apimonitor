package com.wzy.bp.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AlertModel {

	public AlertModel(){
		
	}

	private String level;
	private String logContent;
	private String percentage;
	private String serviceName;
	private String happenTime;

	public void setHappenTime(String happenTime) {
		this.happenTime = happenTime;
	}

	private String  url;

	public String getHappenTime() {
		return happenTime;
	}

	private Integer times;

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getLogContent() {
		return logContent;
	}

	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}
}
