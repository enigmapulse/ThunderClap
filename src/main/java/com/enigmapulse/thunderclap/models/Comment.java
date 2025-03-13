package com.enigmapulse.thunderclap.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username; // the commenter

    @Lob
    private String text;

    private LocalDateTime createdAt = LocalDateTime.now();

    // Many comments belong to one BlogPost
    @ManyToOne
    @JoinColumn(name = "blog_post_id")
    private BlogPost blogPost;
}
