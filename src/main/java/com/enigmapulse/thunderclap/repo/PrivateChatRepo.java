package com.enigmapulse.thunderclap.repo;

import com.enigmapulse.thunderclap.models.PrivateChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivateChatRepo extends JpaRepository<PrivateChatMessage, Integer> {
    List<PrivateChatMessage> findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(String senderId, String receiverId, String senderId2, String receiverId2);
}
