package com.sca.service;

import java.util.List;

import com.sca.entity.Contact;
import com.sca.response.AddContactResponse;
import com.sca.response.FetchContactResponse;

public interface ContactService {

	public AddContactResponse addContact(Contact contact, int userId) throws Exception;

	public FetchContactResponse<List<Contact>> getAllContactByUserId(int userId);

	public String deleteContactById(int userId, int cid);

	public String updateContact(int userId, Contact contact);
}
