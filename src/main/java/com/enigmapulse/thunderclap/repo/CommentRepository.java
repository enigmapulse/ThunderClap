package com.enigmapulse.thunderclap.repo;

import com.enigmapulse.thunderclap.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
