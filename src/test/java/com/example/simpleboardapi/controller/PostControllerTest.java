package com.example.simpleboardapi.controller;

import com.example.simpleboardapi.common.exception.PostNotFoundException;
import com.example.simpleboardapi.domain.Post;
import com.example.simpleboardapi.dto.common.ResponseSavedIdDto;
import com.example.simpleboardapi.dto.post.RequestRegisterPostDto;
import com.example.simpleboardapi.dto.post.RequestUpdatePostDto;
import com.example.simpleboardapi.repository.PostRepository;
import com.example.simpleboardapi.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private PostController postController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글 등록")
    void registerTest() throws Exception {
        //given
        RequestRegisterPostDto requestDto = RequestRegisterPostDto.builder()
                .title("test title")
                .content("test content")
                .build();

        String requestJson = objectMapper.writeValueAsString(requestDto);

        // when, then
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson)
                )
                .andExpect(status().isCreated())
                .andDo(print());

//        String[] location = mvcResult.getResponse().getHeaderValue("Location").toString().split("/");
//        Integer savedId = Integer.parseInt(location[location.length - 1]);
    }

    @Test
    @DisplayName("게시글 등록 실패: 잘못된 제목(빈칸)")
    void registerFailTest() throws Exception {
        //given
        RequestRegisterPostDto requestDto = RequestRegisterPostDto.builder()
                .title("")
                .content("test content")
                .build();

        String requestJson = objectMapper.writeValueAsString(requestDto);

        // when, then
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());

//        String[] location = mvcResult.getResponse().getHeaderValue("Location").toString().split("/");
//        Integer savedId = Integer.parseInt(location[location.length - 1]);
    }

    @Test
    @DisplayName("게시글 등록 실패: 제목에 비속어 들어갈 때")
    void registerFailTest2() throws Exception {
        //given
        RequestRegisterPostDto requestDto = RequestRegisterPostDto.builder()
                .title("비속어")
                .content("test content")
                .build();

        String requestJson = objectMapper.writeValueAsString(requestDto);

        // when, then
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());

//        String[] location = mvcResult.getResponse().getHeaderValue("Location").toString().split("/");
//        Integer savedId = Integer.parseInt(location[location.length - 1]);
    }

    @Test
    @DisplayName("게시글 단건 조회")
    void getPostTest() throws Exception {
        // given
        ResponseSavedIdDto responseSavedIdDto = postService.write(RequestRegisterPostDto.builder()
                .title("test title")
                .content("test content")
                .build());

        Long postId = responseSavedIdDto.getSavedId();

        // expected
        mockMvc.perform(get("/posts/{postId}", postId)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(postId))
                .andExpect(jsonPath("$.title").value("test title"))
                .andExpect(jsonPath("$.content").value("test content"))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 조회 실패 - 잘못된 ID")
    void getPostBadIdTest() throws Exception {
        // given
        ResponseSavedIdDto responseSavedIdDto = postService.write(RequestRegisterPostDto.builder()
                .title("test title")
                .content("test content")
                .build());

        Long postId = responseSavedIdDto.getSavedId();

        // expected
        mockMvc.perform(get("/posts/{postId}", postId + 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect((result) -> {
                    System.out.println("===================");
                    System.out.println(result.getResolvedException().toString());
                    System.out.println("===================");
                    Assertions.assertEquals(result.getResolvedException().getClass().getCanonicalName(), PostNotFoundException.class.getCanonicalName());
                    Assertions.assertTrue(result.getResolvedException().getClass().isAssignableFrom(PostNotFoundException.class));
                    Assertions.assertEquals(result.getResponse().getStatus(), HttpStatus.NOT_FOUND.value());
                })
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 페이징 조회")
    void getPostListTest() throws Exception {
        // given
        List<Post> postList = IntStream.rangeClosed(1, 20).mapToObj(i -> Post.builder()
                        .title("test title" + i)
                        .content("test content" + i)
                        .createdDate(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(postList);

        // expected
        mockMvc.perform(get("/posts?page=1&pageSize=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount", is(20)))
                .andExpect(jsonPath("$.pageSize", is(10)))
                .andExpect(jsonPath("$.postList[0].title").value("test title20"))
                .andExpect(jsonPath("$.postList[0].content").value("test content20"))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정")
    void updatePostTest() throws Exception {
        // given
        Post post = Post.builder()
                .title("test title")
                .content("test content")
                .build();

        Post savedPost = postRepository.save(post);

        RequestUpdatePostDto requestUpdatePostDto = RequestUpdatePostDto.builder()
                .title("test title edit")
                .content("test content edit")
                .build();

        String json = objectMapper.writeValueAsString(requestUpdatePostDto);

        // expected
        mockMvc.perform(put("/posts/{postId}", savedPost.getPostId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 삭제")
    void deletePostTest() throws Exception {
        // given
        Post savedPost = postRepository.save(Post.builder()
                .title("test title")
                .content("test content")
                .build());

        Long postId = savedPost.getPostId();

        // expected
        mockMvc.perform(delete("/posts/{postId}", postId)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}