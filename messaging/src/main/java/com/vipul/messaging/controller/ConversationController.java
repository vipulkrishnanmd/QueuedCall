package com.vipul.messaging.controller;

import com.vipul.messaging.model.Conversation;
import com.vipul.messaging.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/messaging/conversation")
public class ConversationController {
    private final ConversationService conversationService;

    @PostMapping("/")
    public Conversation createConversation(Conversation conversation) {
        return conversationService.createConversation(conversation);
    }

    public Conversation addParticipant(String conversationId, List<String> contactIds) {
        conversationService.addParticipant(conversationId, contactIds);
        return null;
    }
}
