package com.vipul.messaging.service;

import com.vipul.messaging.model.Conversation;
import com.vipul.messaging.repository.ConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;

    public Conversation createConversation(Conversation conversation) {
        return this.conversationRepository.insert(conversation);
    }

    public Conversation getConversation(String conversationId) {
        return conversationRepository.findById(conversationId).orElse(null);
    }

    public void addParticipant(String conversationId, List<String> contactIds) {
        this.conversationRepository.addParticipant(conversationId, contactIds);
    }
}
