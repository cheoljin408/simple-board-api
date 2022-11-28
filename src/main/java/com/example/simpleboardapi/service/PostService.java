package com.example.simpleboardapi.service;

import com.example.simpleboardapi.domain.Post;
import com.example.simpleboardapi.dto.common.RequestListDto;
import com.example.simpleboardapi.dto.common.ResponseSavedIdDto;
import com.example.simpleboardapi.dto.post.RequestRegisterPostDto;
import com.example.simpleboardapi.dto.post.RequestUpdatePostDto;
import com.example.simpleboardapi.dto.post.ResponsePostDto;
import com.example.simpleboardapi.dto.post.ResponsePostListDto;
import com.example.simpleboardapi.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public ResponseSavedIdDto write(RequestRegisterPostDto requestDto) {
        Post post = Post.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .createdDate(LocalDateTime.now())
                .build();

        Post savedPost = postRepository.save(post);

        return ResponseSavedIdDto.builder()
                .savedId(savedPost.getPostId())
                .build();
    }

    public ResponsePostDto get(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시글입니다."));

        return ResponsePostDto.builder()
                .id(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdDate(post.getCreatedDate())
                .build();
    }

    public ResponsePostListDto getList(RequestListDto requestDto) {
        System.out.println("requestDto = " + requestDto.getPage());
        PageRequest pageRequest = PageRequest.of(requestDto.getPage(), requestDto.getPageSize(), Sort.Direction.DESC, "postId");
        Page<Post> postList = postRepository.findAll(pageRequest);

        ResponsePostListDto responsePostListDto = ResponsePostListDto.builder()
                .totalCount((int) postList.getTotalElements())
                .page(postList.getNumber())
                .pageSize(postList.getSize())
                .postList(postList.stream().map(post -> ResponsePostDto.builder()
                        .id(post.getPostId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .createdDate(post.getCreatedDate())
                        .build()).collect(Collectors.toList()))
                .build();

        return responsePostListDto;
    }

    @Transactional
    public void edit(Long postId, RequestUpdatePostDto requestDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시글입니다."));

        post.update(requestDto);
    }

//    public List<Post> getPostList() {
//        return postRepository.findAll();
//    }
//
//    public Optional<Post> getPost(Long postId) {
//        return postRepository.findById(postId);
//    }

//
//    public Optional<Post> delete(Long postId) {
//        Optional<Post> post = postRepository.findById(postId);
//
//        postRepository.delete(post.get());
//
//        return post;
//    }
//
//    public Post update(Long postId, RequestCreatePostDto requestDto) {
//        Optional<Post> post = postRepository.findById(postId);
//
//        post.get().setSubject(requestDto.getSubject());
//        post.get().setContent(requestDto.getContent());
//
//        Post updatedPost = postRepository.save(post.get());
//
//        return updatedPost;
//    }
}
