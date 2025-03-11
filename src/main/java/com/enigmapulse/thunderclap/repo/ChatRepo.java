package com.enigmapulse.thunderclap.repo;

import com.enigmapulse.thunderclap.models.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing operations on the ChatMessage entity.
 *
 * Extends JpaRepository to provide methods for CRUD operations, pagination, and sorting.
 * This interface serves as the data access layer for ChatMessage entities.
 */
@Repository
public interface ChatRepo extends JpaRepository<ChatMessage, Long> {
}
