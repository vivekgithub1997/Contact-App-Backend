package com.sca.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sca.entity.Contact;
import com.sca.entity.User;
import com.sca.repository.ContactRepository;
import com.sca.repository.UserRepository;
import com.sca.response.AddContactResponse;
import com.sca.response.FetchContactResponse;

@Service
public class ContactServiceImpl implements ContactService {

	private static final Logger logger = LoggerFactory.getLogger(ContactServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;

	@Override
	public AddContactResponse addContact(Contact contact, int userId) {
		logger.info("Attempting to add contact for userId: {}", userId);
		try {
			Optional<User> userById = userRepository.findById(userId);
			if (!userById.isPresent()) {
				logger.warn("User not found with ID: {}", userId);
				return new AddContactResponse(false, "User not found with ID: " + userId);
			}

			User user = userById.get();
			contact.setUser(user);
			user.getContacts().add(contact);

			userRepository.save(user);
			logger.info("Contact added successfully for userId: {}", userId);
			return new AddContactResponse(true, "Contact added successfully");

		} catch (Exception e) {
			logger.error("Error while adding contact for userId: {}: {}", userId, e.getMessage(), e);
			return new AddContactResponse(false, "An error occurred while adding contact: " + e.getMessage());
		}
	}

	@Override
	public FetchContactResponse<List<Contact>> getAllContactByUserId(int userId) {
		logger.info("Fetching contacts for userId: {}", userId);
		try {
			Optional<User> userOptional = userRepository.findById(userId);

			if (!userOptional.isPresent()) {
				logger.warn("User not found with ID: {}", userId);
				return new FetchContactResponse<>(false, "User not found with ID: " + userId, null);
			}

			User user = userOptional.get();
			List<Contact> contacts = contactRepository.findByUser(user);

			if (contacts.isEmpty()) {
				logger.info("No contacts found for userId: {}", userId);
				return new FetchContactResponse<>(true, "No contacts found for user ID: " + userId, contacts);
			}

			logger.info("Contacts retrieved successfully for userId: {}", userId);
			return new FetchContactResponse<>(true, "Contacts retrieved successfully", contacts);

		} catch (Exception e) {
			logger.error("Error while fetching contacts for userId: {}: {}", userId, e.getMessage(), e);
			return new FetchContactResponse<>(false, "An error occurred while fetching contacts: " + e.getMessage(),
					null);
		}
	}

	@Override
	public String deleteContactById(int userId, int cid) {
		logger.info("Attempting to delete contact with cid: {} for userId: {}", cid, userId);

		Optional<User> userOptional = userRepository.findById(userId);
		if (userOptional.isEmpty()) {
			logger.warn("User not found with ID: {}", userId);
			return "User not found.";
		}

		Optional<Contact> contactOptional = contactRepository.findById(cid);
		if (contactOptional.isPresent()) {
			Contact contact = contactOptional.get();
			if (contact.getUser().getId() == userId) {
				contactRepository.deleteById(cid);
				logger.info("Contact deleted successfully. cid: {}, userId: {}", cid, userId);
				return "Contact deleted successfully.";
			} else {
				logger.warn("Contact does not belong to userId: {}", userId);
				return "Contact does not belong to the specified user.";
			}
		} else {
			logger.warn("Contact not found with cid: {}", cid);
			return "Contact not found.";
		}
	}
}