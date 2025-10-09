package com.sca.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sca.entity.User;
import com.sca.repository.UserRepository;
import com.sca.request.UserRequest;
import com.sca.response.LoginResponse;

@Service
public class LoginRegisterServiceImpl implements LoginRegisterService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public String register(UserRequest userRequest) {
		// Check if email is already registered
		Optional<User> existingUser = userRepository.findByEmail(userRequest.getEmail());
		if (existingUser.isPresent()) {
			System.out.println("Email Already Register");
			return "User with this email is already registered.";

		}

		// Proceed with registration
		User user = new User();
		user.setEmail(userRequest.getEmail());
		user.setName(userRequest.getName());
		user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		user.setRole("normal-user");
		user.setEnable(true);

		User savedUser = userRepository.save(user);
		if (savedUser != null) {
			return "Registration Successfully..!!";
		} else {
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
				return new LoginResponse(0, false, "Incorrect password", null);
			}
		} else {
			return new LoginResponse(0, false, "User not found", null);
		}
	}

}
