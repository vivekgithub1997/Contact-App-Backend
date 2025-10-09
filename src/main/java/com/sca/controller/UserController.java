package com.sca.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sca.entity.Contact;
import com.sca.response.AddContactResponse;
import com.sca.response.FetchContactResponse;
import com.sca.service.ContactService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/contact")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private ContactService contactService;

	@PostMapping("add-contact/{userId}")
	public ResponseEntity<AddContactResponse> addContact(@PathVariable int userId, @RequestBody Contact contact)
			throws Exception {
		logger.info("Received request to add contact for userId: {}", userId);

		AddContactResponse response = contactService.addContact(contact, userId);

		if (response.isSuccess()) {
			logger.info("Contact added successfully for userId: {}", userId);
			return ResponseEntity.ok(response);
		} else {
			logger.warn("Failed to add contact for userId: {}. Reason: {}", userId, response.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	@GetMapping("getAllContactByUserId/{userId}")
	public ResponseEntity<FetchContactResponse<List<Contact>>> getAllContactsByUserId(@PathVariable int userId) {
		logger.info("Fetching contacts for userId: {}", userId);

		FetchContactResponse<List<Contact>> response = contactService.getAllContactByUserId(userId);

		if (response.isSuccess()) {
			logger.info("Contacts retrieved successfully for userId: {}", userId);
			return ResponseEntity.ok(response);
		} else {
			logger.warn("Failed to retrieve contacts for userId: {}. Reason: {}", userId, response.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	@DeleteMapping("{userId}/{cid}")
	public ResponseEntity<String> deleteContact(@PathVariable int userId, @PathVariable int cid) {
		logger.info("Request to delete contact with cid: {} for userId: {}", cid, userId);

		String result = contactService.deleteContactById(userId, cid);

		switch (result) {
		case "Contact deleted successfully.":
			logger.info("Contact deleted successfully. cid: {}, userId: {}", cid, userId);
			return ResponseEntity.ok(result);
		case "User not found.":
		case "Contact not found.":
		case "Contact does not belong to the specified user.":
			logger.warn("Delete failed for cid: {}, userId: {}. Reason: {}", cid, userId, result);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
		default:
			logger.error("Unexpected error while deleting contact. cid: {}, userId: {}", cid, userId);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error.");
		}
	}
}