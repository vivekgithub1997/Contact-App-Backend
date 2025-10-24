package com.sca.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(LoginRegisterController.class);

	private LoginRegisterService userService;

	public LoginRegisterController(LoginRegisterService userService) {
		super();
		this.userService = userService;
	}

	@PostMapping("/register")
	public ResponseEntity<String> signup(@RequestBody UserRequest userRequest) {
		logger.info("Received registration request for email: {}", userRequest.getEmail());

		String registerMessage = userService.register(userRequest);

		if ("User with this email is already registered.".equals(registerMessage)) {
			logger.warn("Registration failed: {}", registerMessage);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(registerMessage);
		}

		logger.info("Registration successful for email: {}", userRequest.getEmail());
		return ResponseEntity.status(HttpStatus.CREATED).body(registerMessage);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
		logger.info("Login attempt for email: {}", request.getEmail());

		LoginResponse loginResponse = userService.login(request.getEmail(), request.getPassword());

		if (loginResponse != null && loginResponse.isSuccess()) {
			logger.info("Login successful for user: {}", loginResponse.getName());
			return ResponseEntity.ok(loginResponse);
		} else {
			String errorMessage = (loginResponse != null) ? loginResponse.getMessage() : "Invalid login credentials";
			logger.warn("Login failed for email: {}. Reason: {}", request.getEmail(), errorMessage);

			LoginResponse errorResponse = new LoginResponse(0, false, errorMessage, null);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		}
	}
}
