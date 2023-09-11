package com.vipul.messaging.graphql.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.DgsQuery;
import com.vipul.messaging.graphql.DgsConstants;
import com.vipul.messaging.graphql.dataloader.ConversationDataLoader;

import java.util.concurrent.CompletableFuture;

@DgsComponent
public class ConversationDataFetcher {

    @DgsQuery(field = DgsConstants.QUERY.GetConversation)
    public CompletableFuture<Object> getConversation(
            final String id,
            final DgsDataFetchingEnvironment environment
    ) {
        return environment.getDataLoader(ConversationDataLoader.class).load(id);
    }
}
