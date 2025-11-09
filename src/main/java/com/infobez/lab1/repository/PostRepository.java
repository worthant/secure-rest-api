package com.infobez.lab1.repository;

import com.infobez.lab1.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    // Защита от SQL Injection через параметризованный запрос
    @Query("SELECT p FROM Post p WHERE p.authorId = :authorId ORDER BY p.createdAt DESC")
    List<Post> findByAuthorId(@Param("authorId") Long authorId);
    
    @Query("SELECT p FROM Post p ORDER BY p.createdAt DESC")
    List<Post> findAllOrderByCreatedAtDesc();
}