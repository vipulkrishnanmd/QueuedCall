package com.vipul.messaging.graphql.dataloader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsDataLoader;
import com.vipul.messaging.graphql.types.Conversation;
import com.vipul.messaging.graphql.types.ConversationResult;
import com.vipul.messaging.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.dataloader.MappedBatchLoader;
import org.springframework.beans.BeanUtils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@DgsComponent
@DgsDataLoader(name="conversation")
public class ConversationDataLoader implements MappedBatchLoader<String, ConversationResult> {

    private final ConversationService conversationService;
    private final ObjectMapper objectMapper;

    @Override
    public CompletionStage<Map<String, ConversationResult>> load(Set<String> conversationIds) {

        return CompletableFuture.supplyAsync(() -> conversationIds.stream()
                .map(conversationId -> conversationService.getConversation(conversationId))
                .map(c -> {
                            Conversation conversation = objectMapper.convertValue(c, Conversation.class);
                            System.out.println(conversation);
                            return conversation;
                        }
                ).collect(Collectors.toMap(c -> c.getId(), c -> ConversationResult.newBuilder().conversation(c).build())));
    }
}
