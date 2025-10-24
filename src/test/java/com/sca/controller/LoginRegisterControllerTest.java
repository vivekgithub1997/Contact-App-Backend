package com.sca.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sca.request.LoginRequest;
import com.sca.request.UserRequest;
import com.sca.response.LoginResponse;
import com.sca.service.LoginRegisterService;

@WebMvcTest(LoginRegisterController.class)
@AutoConfigureMockMvc(addFilters = false)
public class LoginRegisterControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private LoginRegisterService userService; 

	private ObjectMapper objectMapper;
	private UserRequest userRequest;
	private LoginRequest loginRequest;

	@BeforeEach
	void setup() {
		objectMapper = new ObjectMapper();

		userRequest = new UserRequest();
		userRequest.setEmail("test@example.com");
		userRequest.setName("Test User");
		userRequest.setPassword("password123");

		loginRequest = new LoginRequest();
		loginRequest.setEmail("test@example.com");
		loginRequest.setPassword("password123");
	}

	@Test
	void testRegister_Success() throws Exception {
		Mockito.when(userService.register(any(UserRequest.class))).thenReturn("Registration Successfully..!!");

		mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userRequest))).andExpect(status().isCreated())
				.andExpect(content().string("Registration Successfully..!!"));
	}

	@Test
	void testLogin_Success() throws Exception {
		LoginResponse response = new LoginResponse(1, true, "Login successful", "Test User");

		Mockito.when(userService.login(anyString(), anyString())).thenReturn(response);

		mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true)).andExpect(jsonPath("$.message").value("Login successful"))
				.andExpect(jsonPath("$.name").value("Test User"));
	}

	@Test
	void testLogin_Failure() throws Exception {
		LoginResponse response = new LoginResponse(0, false, "Incorrect password", null);

		Mockito.when(userService.login(anyString(), anyString())).thenReturn(response);

		mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(jsonPath("$.message").value("Incorrect password"))
				.andExpect(jsonPath("$.name").doesNotExist());
	}
}