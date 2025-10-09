package com.sca.response;

public class LoginResponse {
    private int id;
	private boolean success;
	private String message;
	private String name;

	// Constructors
	public LoginResponse() {
	}

	

	public LoginResponse(int id, boolean success, String message, String name) {
		super();
		this.id = id;
		this.success = success;
		this.message = message;
		this.name = name;
	}



	// Getters and Setters
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
