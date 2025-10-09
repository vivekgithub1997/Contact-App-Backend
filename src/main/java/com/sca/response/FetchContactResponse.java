package com.sca.response;

import lombok.Data;

@Data
public class FetchContactResponse<T> {

	private boolean success;
	private String message;
	private T data;

	public FetchContactResponse() {
	}

	public FetchContactResponse(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public FetchContactResponse(boolean success, String message, T data) {
		this.success = success;
		this.message = message;
		this.data = data;
	}

}
