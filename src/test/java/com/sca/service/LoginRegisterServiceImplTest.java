package com.sca.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.sca.entity.User;
import com.sca.repository.UserRepository;
import com.sca.request.UserRequest;
import com.sca.response.LoginResponse;

public class LoginRegisterServiceImplTest {

	@InjectMocks
	private LoginRegisterServiceImpl loginRegisterService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private BCryptPasswordEncoder passwordEncoder;

	private UserRequest userRequest;
	private User mockUser;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);

		userRequest = new UserRequest();
		userRequest.setEmail("test@example.com");
		userRequest.setName("Test User");
		userRequest.setPassword("password123");

		mockUser = new User();
		mockUser.setId(1);
		mockUser.setEmail("test@example.com");
		mockUser.setName("Test User");
		mockUser.setPassword("$2a$10$encodedPassword");
		mockUser.setRole("normal-user");
		mockUser.setEnable(true);
	}

	@Test
	void testRegister_UserAlreadyExists() {
		when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.of(mockUser));

		String result = loginRegisterService.register(userRequest);

		assertEquals("User with this email is already registered.", result);
	}

	@Test
	void testRegister_SuccessfulRegistration() {
		when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
		when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("encodedPassword");
		when(userRepository.save(any(User.class))).thenReturn(mockUser);

		String result = loginRegisterService.register(userRequest);

		assertEquals("Registration Successfully..!!", result);
	}

	@Test
	void testRegister_FailureToSaveUser() {
		when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
		when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("encodedPassword");
		when(userRepository.save(any(User.class))).thenReturn(null);

		String result = loginRegisterService.register(userRequest);

		assertEquals("Not Registered..!!", result);
	}

	@Test
	void testLogin_SuccessfulLogin() {
		when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
		when(passwordEncoder.matches("password123", mockUser.getPassword())).thenReturn(true);

		LoginResponse response = loginRegisterService.login("test@example.com", "password123");

		assertTrue(response.isSuccess());
		assertEquals("Login successful", response.getMessage());
		assertEquals("Test User", response.getName());
	}

	@Test
	void testLogin_IncorrectPassword() {
		when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
		when(passwordEncoder.matches("wrongpassword", mockUser.getPassword())).thenReturn(false);

		LoginResponse response = loginRegisterService.login("test@example.com", "wrongpassword");

		assertFalse(response.isSuccess());
		assertEquals("Incorrect password", response.getMessage());
		assertNull(response.getName());
	}

	@Test
	void testLogin_UserNotFound() {
		when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

		LoginResponse response = loginRegisterService.login("unknown@example.com", "password123");

		assertFalse(response.isSuccess());
		assertEquals("User not found", response.getMessage());
		assertNull(response.getName());
	}
}
