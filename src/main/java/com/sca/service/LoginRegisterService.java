package com.sca.service;

import com.sca.request.UserRequest;
import com.sca.response.LoginResponse;

public interface LoginRegisterService {

	public String register(UserRequest userRequest);

	public LoginResponse login(String userEmail, String userPass);

}
