package com.enigmapulse.thunderclap.services;

import com.enigmapulse.thunderclap.models.BlogPost;
import com.enigmapulse.thunderclap.repo.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BlogService {

    @Autowired
    private BlogPostRepository blogPostRepository;

    public void saveBlogPost(BlogPost blogPost) {
        blogPostRepository.save(blogPost);
    }

    public List<BlogPost> getAllBlogPosts() {
        return blogPostRepository.findAll();
    }

    public Optional<BlogPost> getBlogPostById(Long id) {
        return blogPostRepository.findById(id);
    }

    public void updateLikes(Long id, boolean increment) {
        blogPostRepository.findById(id).ifPresent(post -> {
            if (increment) {
                post.setLikes(post.getLikes() + 1);
            } else {
                post.setDislikes(post.getDislikes() + 1);
            }
            blogPostRepository.save(post);
        });
    }
}
