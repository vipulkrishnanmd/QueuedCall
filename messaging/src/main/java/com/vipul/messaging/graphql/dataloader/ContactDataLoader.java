package com.vipul.messaging.graphql.dataloader;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsDataLoader;
import com.vipul.messaging.graphql.types.Contact;
import com.vipul.messaging.graphql.types.ContactResult;
import com.vipul.messaging.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoaderEnvironment;
import org.dataloader.MappedBatchLoaderWithContext;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@DgsDataLoader(name="contacts")
@DgsComponent()
@RequiredArgsConstructor
public class ContactDataLoader implements MappedBatchLoaderWithContext<String, ContactResult> {

    private final ContactService contactService;

    @Override
    public CompletionStage<Map<String, ContactResult>> load(Set<String> keys, BatchLoaderEnvironment environment) {
        return CompletableFuture.supplyAsync(() ->
                contactService.getContacts(keys).stream().map(c -> ContactResult.newBuilder()
                        .contact(Contact.newBuilder().name(c.getName()).externalRef(c.getExternalRef()).id(c.getId()).build()).build())
                .collect(Collectors.toUnmodifiableMap(
                        c -> c.getContact().getId(),
                        c -> c
                )));
    }
}
