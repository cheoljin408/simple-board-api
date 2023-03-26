package com.example.simpleboardapi.service;

import com.example.simpleboardapi.common.exception.PostNotFoundException;
import com.example.simpleboardapi.domain.Post;
import com.example.simpleboardapi.dto.common.RequestListDto;
import com.example.simpleboardapi.dto.common.ResponseSavedIdDto;
import com.example.simpleboardapi.dto.post.RequestRegisterPostDto;
import com.example.simpleboardapi.dto.post.RequestUpdatePostDto;
import com.example.simpleboardapi.dto.post.ResponsePostDto;
import com.example.simpleboardapi.dto.post.ResponsePostListDto;
import com.example.simpleboardapi.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        assertEquals(postId, responsePostDto.getPostId());
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
        PostNotFoundException postNotFoundException = assertThrows(PostNotFoundException.class,
                () -> postService.get(postId + 1L)
        );
        assertEquals("존재하지 않는 게시글입니다.", postNotFoundException.getMessage());
    }

    @Test
    @DisplayName("게시글 페이징 조회")
    void getListTest() {
        // given
        List<Post> postList = IntStream.rangeClosed(1, 20).mapToObj(i -> Post.builder()
                        .title("test title" + i)
                        .content("test content" + i)
                        .createdDate(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(postList);

        // when
        RequestListDto requestListDto = RequestListDto.builder()
                .page(1)
                .pageSize(10)
                .build();
        ResponsePostListDto responsePostListDto = postService.getList(requestListDto);

        // then
        for (ResponsePostDto responsePostDto : responsePostListDto.getPostList()) {
            System.out.println("responsePostDto.getTitle() = " + responsePostDto.getTitle());
        }
        assertEquals(10, responsePostListDto.getPostList().size());
        assertEquals("test title20", responsePostListDto.getPostList().get(0).getTitle());
    }

    @Test
    @DisplayName("게시글 수정")
    @Transactional
    void editTest() {
        // given
        RequestRegisterPostDto requestRegisterPostDto = RequestRegisterPostDto.builder()
                .title("test title")
                .content("test content")
                .build();

        ResponseSavedIdDto responseSavedIdDto = postService.write(requestRegisterPostDto);
        Post post = postRepository.findById(responseSavedIdDto.getSavedId())
                .orElseThrow(() -> new PostNotFoundException());

        RequestUpdatePostDto requestUpdatePostDto = RequestUpdatePostDto.builder()
                .title("test title edit")
                .content("test content edit")
                .build();

        // when
        post.update(requestUpdatePostDto);

        Post updatedPost = postRepository.findById(responseSavedIdDto.getSavedId())
                .orElseThrow(() -> new PostNotFoundException());

        // then
        assertEquals(updatedPost.getTitle(), "test title edit");
        assertEquals(updatedPost.getContent(), "test content edit");
    }

    @Test
    @DisplayName("게시글 삭제")
    void deleteTest() {
        // given
        Post savedPost = postRepository.save(Post.builder()
                .title("test title")
                .content("test content")
                .build());

        Long postId = savedPost.getPostId();

        // when
        postService.delete(postId);

        // then
        PostNotFoundException postNotFoundException = assertThrows(PostNotFoundException.class,
                () -> postService.get(postId)
        );
        assertEquals("존재하지 않는 게시글입니다.", postNotFoundException.getMessage());
    }
}