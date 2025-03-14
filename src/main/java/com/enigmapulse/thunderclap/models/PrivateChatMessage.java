package com.enigmapulse.thunderclap.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class PrivateChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto orders incoming messages
    private int serial;

    private String senderId;
    private String receiverId;
    private String encryptedMessage;
    private String iv;
}
