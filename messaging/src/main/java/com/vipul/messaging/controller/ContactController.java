package com.vipul.messaging.controller;

import com.vipul.channel.controller.ChannelTestController;
import com.vipul.messaging.model.Contact;
import com.vipul.messaging.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.QueryAnnotation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messaging/contact")
public class ContactController {
    private ChannelTestController testController;
    private ContactService contactService;

    @Autowired
    public ContactController(ChannelTestController testController, ContactService contactService) {
        this.testController = testController;
        this.contactService = contactService;
    }

    @GetMapping("/test")
    public String test() {
        return testController.test();
    }

    @PostMapping("/")
    public Contact createContact(@RequestBody Contact contact) {
        System.out.println(contact.toString());
        return contactService.createContact(contact);
    }
}
