package com.sca.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sca.entity.User;
import com.sca.repository.UserRepository;
import com.sca.request.UserRequest;
import com.sca.response.LoginResponse;

@Service
public class LoginRegisterServiceImpl implements LoginRegisterService {

	private static final Logger logger = LoggerFactory.getLogger(LoginRegisterServiceImpl.class);

	private UserRepository userRepository;

	private BCryptPasswordEncoder passwordEncoder;

	public LoginRegisterServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public String register(UserRequest userRequest) {
		logger.info("Attempting to register user with email: {}", userRequest.getEmail());

		Optional<User> existingUser = userRepository.findByEmail(userRequest.getEmail());
		if (existingUser.isPresent()) {
			logger.warn("Registration failed: Email already registered - {}", userRequest.getEmail());
			return "User with this email is already registered.";
		}

		User user = new User();
		user.setEmail(userRequest.getEmail());
		user.setName(userRequest.getName());
		user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		user.setRole("normal-user");
		user.setEnable(true);

		User savedUser = userRepository.save(user);
		if (savedUser != null) {
			logger.info("User registered successfully: {}", userRequest.getEmail());
			return "Registration Successfully..!!";
		} else {
			logger.error("User registration failed for email: {}", userRequest.getEmail());
			return "Not Registered..!!";
		}
	}

	@Override
	public LoginResponse login(String email, String password) {

		Optional<User> userOpt = userRepository.findByEmail(email);

		if (userOpt.isPresent()) {
			User user = userOpt.get();
			if (passwordEncoder.matches(password, user.getPassword())) {

				return new LoginResponse(user.getId(), true, "Login successful", user.getName());
			} else {
				logger.warn("Login failed: Incorrect password for email: {}", email);
				return new LoginResponse(0, false, "Incorrect password", null);
			}
		} else {
			logger.warn("Login failed: User not found with email: {}", email);
			return new LoginResponse(0, false, "User not found", null);
		}
	}
}