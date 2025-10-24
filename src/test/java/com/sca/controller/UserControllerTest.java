package com.sca.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.sca.entity.Contact;
import com.sca.response.AddContactResponse;
import com.sca.response.FetchContactResponse;
import com.sca.service.ContactService;

class UserControllerTest {

	@Mock
	private ContactService contactService;

	@InjectMocks
	private UserController userController;

	public UserControllerTest() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testAddContactSuccess() throws Exception {
		Contact contact = new Contact();
		AddContactResponse response = new AddContactResponse(true, "Contact added");

		when(contactService.addContact(contact, 1)).thenReturn(response);

		ResponseEntity<AddContactResponse> result = userController.addContact(1, contact);

		assertEquals(200, result.getStatusCodeValue());
		assertEquals("Contact added", result.getBody().getMessage());
	}

	@Test
	void testAddContactFailure() throws Exception {
		Contact contact = new Contact();
		AddContactResponse response = new AddContactResponse(false, "Failed to add contact");

		when(contactService.addContact(contact, 1)).thenReturn(response);

		ResponseEntity<AddContactResponse> result = userController.addContact(1, contact);

		assertEquals(400, result.getStatusCodeValue());
		assertEquals("Failed to add contact", result.getBody().getMessage());
	}

	
	@Test
	void testGetAllContactsSuccess() {
		FetchContactResponse<java.util.List<Contact>> response = new FetchContactResponse<>(true, "Success",
				Collections.emptyList());

		when(contactService.getAllContactByUserId(1)).thenReturn(response);

		ResponseEntity<FetchContactResponse<java.util.List<Contact>>> result = userController.getAllContactsByUserId(1);

		assertEquals(200, result.getStatusCodeValue());
		assertTrue(result.getBody().isSuccess());
	}

	@Test
	void testGetAllContactsFailure() {
		FetchContactResponse<java.util.List<Contact>> response = new FetchContactResponse<>(false, "User not found",
				null);

		when(contactService.getAllContactByUserId(1)).thenReturn(response);

		ResponseEntity<FetchContactResponse<java.util.List<Contact>>> result = userController.getAllContactsByUserId(1);

		assertEquals(400, result.getStatusCodeValue());
		assertFalse(result.getBody().isSuccess());
	}

	@Test
	void testDeleteContactSuccess() {
		when(contactService.deleteContactById(1, 100)).thenReturn("Contact deleted successfully.");

		ResponseEntity<String> result = userController.deleteContact(1, 100);

		assertEquals(200, result.getStatusCodeValue());
		assertEquals("Contact deleted successfully.", result.getBody());
	}

	@Test
	void testDeleteContactUserNotFound() {
		when(contactService.deleteContactById(1, 100)).thenReturn("User not found.");

		ResponseEntity<String> result = userController.deleteContact(1, 100);

		assertEquals(404, result.getStatusCodeValue());
		assertEquals("User not found.", result.getBody());
	}

	@Test
	void testDeleteContactUnexpectedError() {
		when(contactService.deleteContactById(1, 100)).thenReturn("Some unknown error");

		ResponseEntity<String> result = userController.deleteContact(1, 100);

		assertEquals(500, result.getStatusCodeValue());
		assertEquals("Unexpected error.", result.getBody());
	}
}