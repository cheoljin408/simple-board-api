package com.example.simpleboardapi.service;

import com.example.simpleboardapi.domain.Post;
import com.example.simpleboardapi.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public List<Post> getPostList() {
        return postRepository.findAll();
    }

    public Post create(String subject, String content) {
        Post post = new Post();

        post.setSubject(subject);
        post.setContent(content);
        Post savedPost = postRepository.save(post);

        return savedPost;
    }
}
