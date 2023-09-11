package com.vipul.messaging.graphql.mutation;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.DgsMutation;
import com.vipul.messaging.graphql.DgsConstants;
import com.vipul.messaging.graphql.types.ContactResult;
import com.vipul.messaging.graphql.types.CreateContactInput;
import com.vipul.messaging.model.Contact;
import com.vipul.messaging.service.ContactService;
import lombok.RequiredArgsConstructor;

 @DgsComponent
@RequiredArgsConstructor
public class ContactMutation {
    private final ContactService contactService;

    @DgsMutation(field = DgsConstants.MUTATION.CreateContact)
    public ContactResult createContact(CreateContactInput input,
                                       DgsDataFetchingEnvironment environment) {
        Contact contact = Contact.builder()
                .name(input.getName())
                .externalRef(input.getExternalRef())
                .namespace(input.getNamespace())
                .build();
        contact = contactService.createContact(contact);

        return ContactResult.newBuilder().contact(
                com.vipul.messaging.graphql.types.Contact.newBuilder()
                        .name(contact.getName())
                        .externalRef(contact.getExternalRef())
                        .namespace(contact.getNamespace())
                        .id(contact.getId())
                        .build()
        ).build();
    }
}
