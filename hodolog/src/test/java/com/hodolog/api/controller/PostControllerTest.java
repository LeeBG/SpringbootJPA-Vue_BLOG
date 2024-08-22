package com.hodolog.api.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest // MockMvc가 주입이 된다.
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("/posts 요청시 Hello World를 출려한다.")
    void test() throws Exception {
        
        // 글 제목, 글 내용
        // expected
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE) // String으로 넘김
                        .param("title", "글 제목입니다.")
                        .param("content", "글 내용입니다. 하하")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"))
                .andDo(print());
    }
    /*
        mockMVC는 기본적으로 Content-Type을 application/json타입으로 보낸다.
        서버에서 요청을 할때나 혹은 받을 때 받는 HTTP의 헤더값이다. 어떤 데이터의 형태 다라고 알려준다.
        이전에는 application/x-www-form-urlencoded 많이 사용했다.
     */


}