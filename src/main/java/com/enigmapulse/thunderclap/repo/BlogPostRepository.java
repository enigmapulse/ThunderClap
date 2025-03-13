package com.enigmapulse.thunderclap.repo;

import com.enigmapulse.thunderclap.models.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {}
