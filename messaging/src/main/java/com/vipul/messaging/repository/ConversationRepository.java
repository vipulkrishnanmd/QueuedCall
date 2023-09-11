package com.vipul.messaging.repository;

import com.vipul.messaging.model.Contact;
import com.vipul.messaging.model.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends MongoRepository<Conversation, String> {

    @Query("{ '_id' : ?0 }")
    @Update("{'$push': { 'participantIds' : { $each : ?1 } } }")
    void addParticipant(String conversationId, List<String> contactIds);
}