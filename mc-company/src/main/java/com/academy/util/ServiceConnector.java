package com.academy.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ServiceConnector {
	private final Logger logger = LoggerFactory.getLogger(ServiceConnector.class);
	private RestTemplate restTemplate;

	public ServiceConnector(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	public <T> T get(String url, Class<T> responseType) {
		ResponseEntity<T> response = restTemplate.getForEntity(url, responseType);
		if(response.getStatusCodeValue() == 200) {
			return response.getBody();
		}
		return null;
	}
	
	public <T> T getApi(String url, ParameterizedTypeReference<T> type) {
		try {
			ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, null, type);
			if (response.getStatusCodeValue() == 200) {
				return response.getBody();
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return null;
	}
	
	public void delete(String url) {
		restTemplate.delete(url);
	}
}
