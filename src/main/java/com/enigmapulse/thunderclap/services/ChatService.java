package com.enigmapulse.thunderclap.services;

import com.enigmapulse.thunderclap.models.ChatMessage;
import com.enigmapulse.thunderclap.models.PrivateChatMessage;
import com.enigmapulse.thunderclap.repo.ChatRepo;
import com.enigmapulse.thunderclap.repo.PrivateChatRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    // Any messages will be fetched from the repo layer
    @Autowired
    private ChatRepo chatRepo;

    @Autowired
    private PrivateChatRepo privateChatRepo;

    // Return the 'x' most recent messages so that refreshing doesn't destroy our page.
    public List<ChatMessage> getTopMessages() {
        // You can change the number of messages that are shown on the screen here by changing the second argument
        Pageable pageable = PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC, "Serial"));
        return chatRepo.findAll(pageable).getContent();
    }

    // Saving messages every time they're sent
    public void saveMessage(ChatMessage chatMessage) {
        chatRepo.save(chatMessage);
    }

    // The messages are stored in encrypted form so even the server cannot decode what's inside
    public void savePrivateMessage(PrivateChatMessage privateChatMessage) {
        privateChatRepo.save(privateChatMessage);
    }

    public List<PrivateChatMessage> getMessageHistory(String user1, String user2) {
        return privateChatRepo.findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(user1, user2, user2, user1);
    }
}
