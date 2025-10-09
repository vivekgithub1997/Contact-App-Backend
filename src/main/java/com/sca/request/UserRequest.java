package com.sca.request;

import lombok.Data;

@Data
public class UserRequest {
	
    private String name;
	private String email;
	private String password;
	private String role;
	private boolean enable;

}
