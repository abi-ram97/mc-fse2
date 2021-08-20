package com.academy.model;

import lombok.Builder;
import lombok.Data;

@Data
public class ServiceResponse<T> {
	private T body;
	
	private int status;
	
	public ServiceResponse() {}

	@Builder
	public ServiceResponse(T body, int status) {
		this.body = body;
		this.status = status;
	}
	
	

}
