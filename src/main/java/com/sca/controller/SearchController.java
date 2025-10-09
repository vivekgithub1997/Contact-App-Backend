package com.sca.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.sca.entity.Contact;
import com.sca.entity.User;
import com.sca.repository.ContactRepository;
import com.sca.repository.UserRepository;

@RestController
public class SearchController 
{
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContactRepository contactRepository;

	//search Handler
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query,Principal principal)
	{
		
		System.out.println(query);
		User user=this.userRepository.getUserByUserName(principal.getName());
		
		List<Contact> contacts=this.contactRepository.findByNameContainingAndUser(query, user);
		return ResponseEntity.ok(contacts);
		
	}
}