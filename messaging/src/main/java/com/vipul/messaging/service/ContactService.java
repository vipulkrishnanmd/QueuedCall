package com.vipul.messaging.service;

import com.vipul.messaging.model.Contact;
import com.vipul.messaging.repository.ContactRepository;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ContactService {
    ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Contact createContact(Contact contact) {
        return contactRepository.insert(contact);
    }

    public Contact getContact(String contactId) {
        try {
            return contactRepository.findById(contactId).orElseThrow(() -> new Exception("Not Found"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Contact> getContacts(Set<String> contactIds) {
        return StreamSupport.stream(contactRepository.findAllById(contactIds).spliterator(), false)
                .collect(Collectors.toList());
    }
}
