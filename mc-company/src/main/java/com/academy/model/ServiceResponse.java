package com.academy.model;

import lombok.Builder;

public class ServiceResponse<T> {
	private T body;
	private int status;
	
	public ServiceResponse() {}
	
	@Builder
	public ServiceResponse(T body, int status) {
		this.body = body;
		this.status = status;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
