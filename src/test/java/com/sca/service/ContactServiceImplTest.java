package com.sca.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.sca.entity.Contact;
import com.sca.entity.User;
import com.sca.repository.ContactRepository;
import com.sca.repository.UserRepository;
import com.sca.response.AddContactResponse;
import com.sca.response.FetchContactResponse;

@SpringBootTest
public class ContactServiceImplTest {

	@InjectMocks
	private ContactServiceImpl contactService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private ContactRepository contactRepository;

	private User mockUser;
	private Contact mockContact;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		mockUser = new User();
		mockUser.setId(1);
		mockUser.setContacts(new ArrayList<>());

		mockContact = new Contact();
		mockContact.setCId(101);
		mockContact.setUser(mockUser);
	}

	@Test
	void testAddContact_UserExists() {
		when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));

		AddContactResponse response = contactService.addContact(mockContact, 1);

		assertTrue(response.isSuccess());
		assertEquals("Contact added successfully", response.getMessage());
		verify(userRepository, times(1)).save(mockUser);
	}

	@Test
	void testAddContact_UserNotFound() {
		when(userRepository.findById(1)).thenReturn(Optional.empty());

		AddContactResponse response = contactService.addContact(mockContact, 1);

		assertFalse(response.isSuccess());
		assertEquals("User not found with ID: 1", response.getMessage());
	}

	@Test
	void testGetAllContactByUserId_UserExistsWithContacts() {
		List<Contact> contactList = Arrays.asList(mockContact);
		when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));
		when(contactRepository.findByUser(mockUser)).thenReturn(contactList);

		FetchContactResponse<List<Contact>> response = contactService.getAllContactByUserId(1);

		assertTrue(response.isSuccess());
		assertEquals("Contacts retrieved successfully", response.getMessage());
		assertEquals(1, response.getData().size());
	}

	
	@Test
	void testGetAllContactByUserId_UserNotFound() {
		when(userRepository.findById(1)).thenReturn(Optional.empty());

		FetchContactResponse<List<Contact>> response = contactService.getAllContactByUserId(1);

		assertFalse(response.isSuccess());
		assertEquals("User not found with ID: 1", response.getMessage());
		assertNull(response.getData());
	}
	
	

	@Test
	void testDeleteContactById_Success() {
		when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));
		when(contactRepository.findById(101)).thenReturn(Optional.of(mockContact));

		String result = contactService.deleteContactById(1, 101);

		assertEquals("Contact deleted successfully.", result);
		verify(contactRepository, times(1)).deleteById(101);
	}
	
	

	@Test
	void testDeleteContactById_ContactNotBelongToUser() {
		User anotherUser = new User();
		anotherUser.setId(2);
		mockContact.setUser(anotherUser);

		when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));
		when(contactRepository.findById(101)).thenReturn(Optional.of(mockContact));

		String result = contactService.deleteContactById(1, 101);

		assertEquals("Contact does not belong to the specified user.", result);
	}

	@Test
	void testDeleteContactById_ContactNotFound() {
		when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));
		when(contactRepository.findById(101)).thenReturn(Optional.empty());

		String result = contactService.deleteContactById(1, 101);

		assertEquals("Contact not found.", result);
	}

	@Test
	void testDeleteContactById_UserNotFound() {
		when(userRepository.findById(1)).thenReturn(Optional.empty());

		String result = contactService.deleteContactById(1, 101);

		assertEquals("User not found.", result);
	}
}