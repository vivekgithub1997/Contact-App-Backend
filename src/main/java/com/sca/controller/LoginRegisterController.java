package com.sca.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sca.request.LoginRequest;
import com.sca.request.UserRequest;
import com.sca.response.LoginResponse;
import com.sca.service.LoginRegisterService;

@RestController
@RequestMapping("/api/auth")
public class LoginRegisterController {

	@Autowired
	private LoginRegisterService userService;

	@PostMapping("/register")
	public ResponseEntity<String> signup(@RequestBody UserRequest userRequest) {

		String register = userService.register(userRequest);

		return new ResponseEntity<String>(register, HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
		LoginResponse loginResponse = userService.login(request.getEmail(), request.getPassword());

		if (loginResponse != null && loginResponse.isSuccess()) {
			System.out.println(loginResponse.getMessage() + " | Welcome: " + loginResponse.getName());
			return ResponseEntity.ok(loginResponse);
		} else {
			String errorMessage = (loginResponse != null) ? loginResponse.getMessage() : "Invalid login credentials";

			System.out.println("Login failed: " + errorMessage);

			// Return a response with default id = 0 and null name
			LoginResponse errorResponse = new LoginResponse(0, false, errorMessage, null);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		}

	}
}