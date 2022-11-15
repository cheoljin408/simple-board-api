package com.example.simpleboardapi.controller;

import com.example.simpleboardapi.dto.post.RequestRegisterPostDto;
import com.example.simpleboardapi.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

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
}