package com.enigmapulse.thunderclap.controllers;

import com.enigmapulse.thunderclap.models.ChatMessage;
import com.enigmapulse.thunderclap.models.PrivateChatMessage;
import com.enigmapulse.thunderclap.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;
import java.util.List;

@RestController
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // A Service for dealing with the chat messages
    @Autowired
    private ChatService chatService;

    // Handle a user joining the chat
    @MessageMapping("/chat.register")
    @SendTo("/topic/public")
    public ChatMessage register(@Payload ChatMessage chatMessage) {
        chatMessage.setType(ChatMessage.MessageType.JOIN);
        return chatMessage;
    }

    // Handle sending a chat message
    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        chatMessage.setType(ChatMessage.MessageType.CHAT);

        // Whenever a message is sent, I want it to be stored on our database.
        // Usually, IndexedDB or whatever is used to store data on the clients machine so that
        // the server doesn't end up using too much storage, but here there's no problem
        chatService.saveMessage(chatMessage);
        return chatMessage;
    }

    // Retaining page data on refreshing the page
    @GetMapping("/old-messages")
    public List<ChatMessage> getTopMessages() {
        return chatService.getTopMessages();
    }


    // Working: Messages sent to private-message are routed to {recipientUsername}/queue/messages. If you are Alice, you will subscribe to user/queue/messages
    // Note that in the front-end, the stompClient will automatically replace 'user' so don't worry about it.
    @MessageMapping("/private-message")
    public void sendPrivateMessage(PrivateChatMessage message) {
        String recipientUsername = message.getRecipient();
        messagingTemplate.convertAndSendToUser(recipientUsername, "/queue/messages", message);
    }
}
