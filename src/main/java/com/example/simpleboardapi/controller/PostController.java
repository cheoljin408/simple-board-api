package com.example.simpleboardapi.controller;

import com.example.simpleboardapi.domain.Post;
import com.example.simpleboardapi.dto.common.ResponseSavedIdDto;
import com.example.simpleboardapi.dto.post.PostDto;
import com.example.simpleboardapi.dto.post.RequestCreatePostDto;
import com.example.simpleboardapi.dto.post.ResponsePostListDto;
import com.example.simpleboardapi.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<?> postList() {
        List<Post> postList = postService.getPostList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponsePostListDto.builder()
                        .postList(postList.stream().map(
                                post -> PostDto.builder()
                                        .id(post.getId())
                                        .subject(post.getSubject())
                                        .content(post.getContent())
                                        .createdDate(post.getCreatedDate())
                                        .build()
                        ).collect(Collectors.toList())).build()
                );
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> post(@PathVariable Long postId) {
        Optional<Post> post = postService.getPost(postId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(PostDto.builder()
                        .id(post.get().getId())
                        .subject(post.get().getSubject())
                        .content(post.get().getContent())
                        .createdDate(post.get().getCreatedDate())
                        .build()
                );
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody RequestCreatePostDto requestDto) {
        Post savedPost = postService.create(requestDto.getSubject(), requestDto.getContent());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedPost.getId()).toUri();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(location)
                .body(ResponseSavedIdDto.builder()
                        .savedId(savedPost.getId())
                        .build()
                );
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        postService.delete(postId);
    }
}
