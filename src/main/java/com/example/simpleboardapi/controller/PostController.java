package com.example.simpleboardapi.controller;

import com.example.simpleboardapi.domain.Post;
import com.example.simpleboardapi.dto.common.ResponseSavedIdDto;
import com.example.simpleboardapi.dto.post.RequestCreatePostDto;
import com.example.simpleboardapi.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody RequestCreatePostDto requestDto) {
        Post savedPost = postService.create(requestDto.getSubject(), requestDto.getContent());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedPost.getId()).toUri();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(location)
                .body(ResponseSavedIdDto.builder()
                        .savedId(savedPost.getId())
                        .build());
    }
}
