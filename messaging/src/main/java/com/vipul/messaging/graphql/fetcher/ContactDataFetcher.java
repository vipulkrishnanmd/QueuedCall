package com.vipul.messaging.graphql.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.vipul.messaging.graphql.DgsConstants;
import com.vipul.messaging.graphql.dataloader.ContactDataLoader;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;

import java.util.concurrent.CompletableFuture;

@DgsComponent
public class ContactDataFetcher {

    @DgsQuery(field = DgsConstants.QUERY.GetContact)
    public CompletableFuture<Object> getContact(
            final String id, final DgsDataFetchingEnvironment environment) {

        return environment.getDataLoader(ContactDataLoader.class).load(id);
    }
}
