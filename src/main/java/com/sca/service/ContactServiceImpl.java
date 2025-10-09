package com.sca.service;

import java.util.List;
import java.util.Optional;

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

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;

	@Override
	public AddContactResponse addContact(Contact contact, int userId) {
		try {
			Optional<User> userById = userRepository.findById(userId);
			if (!userById.isPresent()) {
				return new AddContactResponse(false, "User not found with ID: " + userId);
			}

			User user = userById.get();

			// Associate contact with user
			contact.setUser(user);
			user.getContacts().add(contact);

			userRepository.save(user);
			// contactRepository.save(contact);

			return new AddContactResponse(true, "Contact added successfully");

		} catch (Exception e) {
			e.printStackTrace();
			return new AddContactResponse(false, "An error occurred while adding contact: " + e.getMessage());
		}
	}

	@Override
	public FetchContactResponse<List<Contact>> getAllContactByUserId(int userId) {

		try {
			Optional<User> userOptional = userRepository.findById(userId);

			if (!userOptional.isPresent()) {
				return new FetchContactResponse<>(false, "User not found with ID: " + userId, null);
			}

			User user = userOptional.get();
			List<Contact> contacts = contactRepository.findByUser(user);

			if (contacts.isEmpty()) {
				return new FetchContactResponse<>(true, "No contacts found for user ID: " + userId, contacts);
			}

			return new FetchContactResponse<>(true, "Contacts retrieved successfully", contacts);

		} catch (Exception e) {
			e.printStackTrace();
			return new FetchContactResponse<>(false, "An error occurred while fetching contacts: " + e.getMessage(),
					null);
		}

	}

	@Override
	public String deleteContactById(int userId, int cid) {
	    Optional<User> userOptional = userRepository.findById(userId);
	    if (userOptional.isEmpty()) {
	        return "User not found.";
	    }

	    Optional<Contact> contactOptional = contactRepository.findById(cid);
	    if (contactOptional.isPresent()) {
	        Contact contact = contactOptional.get();
	        if (contact.getUser().getId() == userId) {
	            contactRepository.deleteById(cid);
	            return "Contact deleted successfully.";
	        } else {
	            return "Contact does not belong to the specified user.";
	        }
	    } else {
	        return "Contact not found.";
	    }
	}
}