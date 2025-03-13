package com.enigmapulse.thunderclap.controllers;

import com.enigmapulse.thunderclap.models.BlogPost;
import com.enigmapulse.thunderclap.models.Comment;
import com.enigmapulse.thunderclap.services.BlogService;
import com.enigmapulse.thunderclap.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;

    // Display list of blog posts
    @GetMapping
    public String listBlogs(Model model) {
        model.addAttribute("blogs", blogService.getAllBlogPosts());
        return "blogs/list"; // Thymeleaf template: blogs/list.html
    }

    // Show form to create a new blog post
    @GetMapping("/new")
    public String showNewBlogForm(Model model) {
        model.addAttribute("blogPost", new BlogPost());
        return "blogs/new"; // Thymeleaf template: blogs/new.html
    }

    // Handle blog post submission
    @PostMapping
    public String createBlog(@ModelAttribute BlogPost blogPost, Authentication authentication) {
        // Set the blog post author from the authenticated user
        blogPost.setTitle(blogPost.getTitle()); // Already bound from form
        // Optionally, store the username as part of blog post if needed
        // blogPost.setAuthor(authentication.getName());
        blogService.saveBlogPost(blogPost);
        return "redirect:/blogs";
    }

    // View a single blog post along with its comments
    @GetMapping("/view/{id}")
    public String viewBlog(@PathVariable Long id, Model model) {
        Optional<BlogPost> blogPost = blogService.getBlogPostById(id);
        if (blogPost.isPresent()) {
            model.addAttribute("blogPost", blogPost.get());
            model.addAttribute("newComment", new Comment());
            return "blogs/view"; // Thymeleaf template: blogs/view.html
        }
        return "redirect:/blogs";
    }


    // Like/Dislike functionality (for simplicity, using GET or POST endpoints)
    @PostMapping("/{id}/like")
    public String likeBlog(@PathVariable Long id) {
        blogService.updateLikes(id, true);
        return "redirect:/blogs/view/" + id;  // Correct redirect URL
    }

    @PostMapping("/{id}/dislike")
    public String dislikeBlog(@PathVariable Long id) {
        blogService.updateLikes(id, false);
        return "redirect:/blogs/view/" + id;  // Correct redirect URL
    }


    // Post a comment
    @PostMapping("/{id}/comment")
    public String postComment(@PathVariable Long id, @RequestParam("text") String text, Authentication authentication) {
        blogService.getBlogPostById(id).ifPresent(blogPost -> {
            Comment newComment = new Comment();
            newComment.setText(text);
            newComment.setBlogPost(blogPost);
            newComment.setUsername(authentication.getName());
            // Ensure the id is null (it should be for a new entity)
            commentService.saveComment(newComment);
        });
        return "redirect:/blogs/view/" + id;
    }

}
