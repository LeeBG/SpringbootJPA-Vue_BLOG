package com.hodolog.api.controller;

import com.hodolog.api.domain.Post;
import com.hodolog.api.repository.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    // @BeforeEach : 뭔가 test메서드들이 실행되기 전에 항상 수행이 되도록 보장해주는 어노테이션이다.
    @BeforeEach
    void cleanPost(){
        postRepository.deleteAll();
    }


    @Test // 실패에 대한 Test
    @DisplayName("/posts 요청시 Hello World를 출력한다.")
    void test() throws Exception {
        //when
        // 글 제목, 글 내용
        // expected
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)        // 사실 기본값이 아니였음 안쓰면 415에러conteny type지원에러
                        .content("{\"title\": \"제목입니다.\", \"content\": \"내용입니다.\"}")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("{}"))
                .andDo(print());
    }
    /*
        mockMVC는 기본적으로 Content-Type을 application/json타입으로 보낸다.
        서버에서 요청을 할때나 혹은 받을 때 받는 HTTP의 헤더값이다. 어떤 데이터의 형태 다라고 알려준다.
        이전에는 application/x-www-form-urlencoded 많이 사용했다.
     */

    @Test
    @DisplayName("/posts 요청시 title값은 필수다.")
    void test2() throws Exception {
        // 글 제목, 글 내용
        // expected
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)        // 사실 기본값이 아니였음 안쓰면 415에러conteny type지원에러
                        .content("{\"title\": null, \"content\": \"내용입니다.\"}")
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(print());
    }


    @Test
    @DisplayName("/posts 요청시 title값은 필수다.")
    void test3() throws Exception {

        // 글 제목, 글 내용
        // expected
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)        // 사실 기본값이 아니였음 안쓰면 415에러conteny type지원에러
                        .content("{\"title\": \"제목입니다.\", \"content\": \"내용입니다.\"}")
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        // DB에 저장읻 돼야한다.
        assertEquals(1L,postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }
    // 테스트 클래스 전체를 돌리면 앞의 test1에서 성공case때문에 기대값 1이 만족이 아닌 2가 되면서 테스트 실패된다.
    // 그렇기 때문에 test코드 돌리기 전에 모든 데이터들을 미리 삭제해보는 것도 좋은 방법이다.
}