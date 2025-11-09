package com.infobez.lab1.service;

import com.infobez.lab1.model.Post;
import com.infobez.lab1.model.User;
import com.infobez.lab1.dto.PostRequest;
import com.infobez.lab1.dto.PostResponse;
import com.infobez.lab1.repository.PostRepository;
import com.infobez.lab1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    
    // Политика санитизации для защиты от XSS
    private final PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.BLOCKS);
    
    @Transactional
    public PostResponse createPost(PostRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Санитизация данных для защиты от XSS
        String sanitizedTitle = policy.sanitize(request.getTitle());
        String sanitizedContent = policy.sanitize(request.getContent());
        
        Post post = new Post();
        post.setTitle(sanitizedTitle);
        post.setContent(sanitizedContent);
        post.setAuthorId(user.getId());
        
        Post savedPost = postRepository.save(post);
        return convertToResponse(savedPost, user.getUsername());
    }
    
    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAllOrderByCreatedAtDesc().stream()
                .map(post -> {
                    User author = userRepository.findById(post.getAuthorId())
                            .orElse(null);
                    return convertToResponse(post, author != null ? author.getUsername() : "Unknown");
                })
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
        
        User author = userRepository.findById(post.getAuthorId())
                .orElse(null);
        
        return convertToResponse(post, author != null ? author.getUsername() : "Unknown");
    }
    
    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return postRepository.findByAuthorId(user.getId()).stream()
                .map(post -> convertToResponse(post, username))
                .collect(Collectors.toList());
    }
    
    private PostResponse convertToResponse(Post post, String authorUsername) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthorId(),
                authorUsername,
                post.getCreatedAt()
        );
    }
}