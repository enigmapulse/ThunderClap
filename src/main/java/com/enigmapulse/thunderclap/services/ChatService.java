package com.enigmapulse.thunderclap.services;

import com.enigmapulse.thunderclap.models.ChatMessage;
import com.enigmapulse.thunderclap.repo.ChatRepo;
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

    // Return the 'x' most recent messages so that refreshing doesn't destroy our page.
    public List<ChatMessage> getTopMessages() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "Serial"));
        return chatRepo.findAll(pageable).getContent();
    }

    // Saving messages every time they're sent
    public void saveMessage(ChatMessage chatMessage) {
        chatRepo.save(chatMessage);
    }
}
