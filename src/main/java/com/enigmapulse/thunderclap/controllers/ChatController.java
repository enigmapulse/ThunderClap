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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        // If imageBase64 is provided, treat it as an image message
        if (chatMessage.getImageBase64() != null) {
            chatMessage.setType(ChatMessage.MessageType.IMAGE);
        }
        // If audioBase64 is provided, treat it as an audio message
        else if (chatMessage.getAudioBase64() != null) {
            chatMessage.setType(ChatMessage.MessageType.AUDIO);
        }
        else {
            chatMessage.setType(ChatMessage.MessageType.CHAT);
        }
        // Save the message (works for both text and image messages)
        chatService.saveMessage(chatMessage);
        return chatMessage;
    }



    // Retaining page data on refreshing the page
    @GetMapping("/old-messages")
    public List<ChatMessage> getTopMessages() {
        return chatService.getTopMessages();
    }

    // Sending the message history to the sender and receiver's urls
    // Note that anyone can access the ENCRYPTED version of the two people's messages, which cannot be decrypted easily
    @GetMapping("/history")
    public List<PrivateChatMessage> getChatHistoryPrivate(@RequestParam String user1, @RequestParam String user2) {
        return chatService.getMessageHistory(user1, user2);
    }

    // Working: Messages sent to private-message are routed to {recipientUsername}/queue/messages. If you are Alice, you will subscribe to user/queue/messages
    // Note that in the front-end, the stompClient will automatically replace 'user' so don't worry about it.
    @MessageMapping("/private-message")
    public void sendPrivateMessage(PrivateChatMessage privateMessage) {
        // First, save the message in a database
        chatService.savePrivateMessage(privateMessage);

        String recipientUsername = privateMessage.getReceiverId();
        messagingTemplate.convertAndSendToUser(recipientUsername, "/queue/messages", privateMessage);

    }
}
