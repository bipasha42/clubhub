package com.example.clubhub.repository;


import com.example.clubhub.model.Comment;
import com.example.clubhub.model.Post;
import com.example.clubhub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findByPost(Post post);
    List<Comment> findByUser(User user);
    int countByPost(Post post);
}

