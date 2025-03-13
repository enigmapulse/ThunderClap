package com.enigmapulse.thunderclap.services;

import com.enigmapulse.thunderclap.models.Comment;
import com.enigmapulse.thunderclap.repo.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }
}
