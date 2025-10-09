package com.sca.response;

import lombok.Data;

@Data
public class AddContactResponse {
	private boolean success;
	private String message;

	public AddContactResponse() {
	}

	public AddContactResponse(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

}