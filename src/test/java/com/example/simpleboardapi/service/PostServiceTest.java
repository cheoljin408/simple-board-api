package com.example.simpleboardapi.service;

import com.example.simpleboardapi.domain.Post;
import com.example.simpleboardapi.dto.common.ResponseSavedIdDto;
import com.example.simpleboardapi.dto.post.RequestRegisterPostDto;
import com.example.simpleboardapi.dto.post.ResponsePostDto;
import com.example.simpleboardapi.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글 쓰기")
    void writeTest() {
        // given
        RequestRegisterPostDto requestDto = RequestRegisterPostDto.builder()
                .title("test title")
                .content("test content")
                .build();

        // when
        ResponseSavedIdDto responseSavedIdDto = postService.write(requestDto);

        // then
        Post post = postRepository.findById(responseSavedIdDto.getSavedId())
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));

        assertEquals(responseSavedIdDto.getSavedId(), post.getPostId());
        assertEquals("test title", post.getTitle());
        assertEquals("test content", post.getContent());
    }

    @Test
    @DisplayName("게시글 단건 조회")
    void getTest() {
        // given
        ResponseSavedIdDto responseSavedIdDto = postService.write(RequestRegisterPostDto.builder()
                .title("test title")
                .content("test content")
                .build());

        Long postId = responseSavedIdDto.getSavedId();

        // when
        ResponsePostDto responsePostDto = postService.get(postId);

        // then
        assertEquals(postId, responsePostDto.getId());
        assertEquals("test title", responsePostDto.getTitle());
        assertEquals("test content", responsePostDto.getContent());
    }

    @Test
    @DisplayName("게시글 조회 실패 - 잘못된 ID")
    void getFailBadIdTest() {
        // given
        ResponseSavedIdDto responseSavedIdDto = postService.write(RequestRegisterPostDto.builder()
                .title("test title")
                .content("test content")
                .build());

        Long postId = responseSavedIdDto.getSavedId();

        // when, then
        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> postService.get(postId + 1L)
        );
        assertEquals("존재하지 않는 게시글입니다.", runtimeException.getMessage());
    }
}