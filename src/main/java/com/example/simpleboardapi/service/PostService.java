package com.example.simpleboardapi.service;

import com.example.simpleboardapi.domain.Post;
import com.example.simpleboardapi.dto.post.RequestCreatePostDto;
import com.example.simpleboardapi.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public List<Post> getPostList() {
        return postRepository.findAll();
    }

    public Optional<Post> getPost(Long postId) {
        return postRepository.findById(postId);
    }

    public Post create(String subject, String content) {
        Post post = new Post();

        post.setSubject(subject);
        post.setContent(content);
        Post savedPost = postRepository.save(post);

        return savedPost;
    }

    public Optional<Post> delete(Long postId) {
        Optional<Post> post = postRepository.findById(postId);

        postRepository.delete(post);

        return post;
    }

    public Post update(Long postId, RequestCreatePostDto requestDto) {
        Optional<Post> post = postRepository.findById(postId);

        post.get().setSubject(requestDto.getSubject());
        post.get().setContent(requestDto.getContent());

        Post updatedPost = postRepository.save(post.get());

        return updatedPost;
    }
}
