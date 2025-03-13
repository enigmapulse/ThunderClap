package com.enigmapulse.thunderclap.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto orders incoming messages
    private int serial; // When I'm reloading all the messages, each message has a serial number

    // Getters and setters
    @Getter
    @Setter
    private String sender;

    @Getter
    @Setter
    private String content;

    @Getter
    @Setter
    private MessageType type;

    @Lob
    @Getter @Setter
    private String imageBase64;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE,
        IMAGE
    }

}

