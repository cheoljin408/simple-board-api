package com.example.simpleboardapi.controller;

import com.example.simpleboardapi.common.exception.InvalidRequestException;
import com.example.simpleboardapi.dto.common.RequestListDto;
import com.example.simpleboardapi.dto.common.ResponseSavedIdDto;
import com.example.simpleboardapi.dto.post.RequestRegisterPostDto;
import com.example.simpleboardapi.dto.post.RequestUpdatePostDto;
import com.example.simpleboardapi.dto.post.ResponsePostDto;
import com.example.simpleboardapi.dto.post.ResponsePostListDto;
import com.example.simpleboardapi.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<ResponseSavedIdDto> registerPost(@Valid @RequestBody RequestRegisterPostDto requestDto) {
        if (!requestDto.isValidate()) {
            throw new InvalidRequestException("title", "제목에는 비속어가 들어갈 수 없습니다.");
        }
        ResponseSavedIdDto responseSavedIdDto = postService.write(requestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{savedId}")
                .buildAndExpand(responseSavedIdDto.getSavedId())
                .toUri();

        return ResponseEntity.created(location).body(responseSavedIdDto);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ResponsePostDto> getPost(@PathVariable Long postId) {
        ResponsePostDto responsePostDto = postService.get(postId);

        return ResponseEntity.ok(responsePostDto);
    }

    @GetMapping
    public ResponseEntity<ResponsePostListDto> getPostList(@ModelAttribute RequestListDto requestDto) {
        ResponsePostListDto responsePostListDto = postService.getList(requestDto);

        return ResponseEntity.ok(responsePostListDto);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable Long postId, @RequestBody RequestUpdatePostDto requestUpdatePostDto) {
        postService.edit(postId, requestUpdatePostDto);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        postService.delete(postId);

        return ResponseEntity.noContent().build();
    }

//    @GetMapping
//    public ResponseEntity<?> postList() {
//        List<Post> postList = postService.getPostList();
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(ResponsePostListDto.builder()
//                        .postList(postList.stream().map(
//                                post -> ResponsePostDto.builder()
//                                        .id(post.getId())
//                                        .subject(post.getSubject())
//                                        .content(post.getContent())
//                                        .createdDate(post.getCreatedDate())
//                                        .build()
//                        ).collect(Collectors.toList())).build()
//                );
//    }

//    @GetMapping("/{postId}")
//    public ResponseEntity<?> post(@PathVariable Long postId) {
//        Optional<Post> post = postService.getPost(postId);
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(ResponsePostDto.builder()
//                        .id(post.get().getId())
//                        .subject(post.get().getSubject())
//                        .content(post.get().getContent())
//                        .createdDate(post.get().getCreatedDate())
//                        .build()
//                );
//    }

//    @PutMapping("/{postId}")
//    public ResponseEntity<?> updatePost(@PathVariable Long postId, @RequestBody RequestCreatePostDto requestDto) {
//        Post updatedPost = postService.update(postId, requestDto);
//
//        URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .location(location)
//                .body(ResponseSavedIdDto.builder()
//                        .savedId(updatedPost.getId())
//                        .build()
//                );
//    }

//    @DeleteMapping("/{postId}")
//    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
//        Optional<Post> post = postService.delete(postId);
//
//        return ResponseEntity
//                .status(HttpStatus.NO_CONTENT).build();
//    }
}
