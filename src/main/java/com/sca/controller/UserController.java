package com.sca.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sca.entity.Contact;
import com.sca.response.AddContactResponse;
import com.sca.response.FetchContactResponse;
import com.sca.service.ContactService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/contact")
public class UserController {

	@Autowired
	private ContactService contactService;

	@PostMapping("add-contact/{userId}")
	public ResponseEntity<AddContactResponse> addContact(@PathVariable int userId, @RequestBody Contact contact)
			throws Exception {

		AddContactResponse response = contactService.addContact(contact, userId);

		if (response.isSuccess()) {
			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	@GetMapping("getAllContactByUserId/{userId}")
	public ResponseEntity<FetchContactResponse<List<Contact>>> getAllContactsByUserId(@PathVariable int userId) {
		FetchContactResponse<List<Contact>> response = contactService.getAllContactByUserId(userId);

		if (response.isSuccess()) {
			// System.out.println(response);
			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	@DeleteMapping("{userId}/{cid}")
	public ResponseEntity<String> deleteContact(@PathVariable int userId, @PathVariable int cid) {

		String result = contactService.deleteContactById(userId, cid);

		switch (result) {
		case "Contact deleted successfully.":
			return ResponseEntity.ok(result);
		case "User not found.":
		case "Contact not found.":
		case "Contact does not belong to the specified user.":
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
		default:
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error.");
		}

	}
}