package com.example.simpleboardapi.repository;

import com.example.simpleboardapi.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    void delete(Post post);
}
