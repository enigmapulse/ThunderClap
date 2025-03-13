package com.enigmapulse.thunderclap.repo;

import com.enigmapulse.thunderclap.models.PrivateChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrivateChatRepo extends JpaRepository<PrivateChatMessage, Integer> {
    List<PrivateChatMessage> findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(String senderId, String receiverId, String receiverId2, String senderId2);
}
