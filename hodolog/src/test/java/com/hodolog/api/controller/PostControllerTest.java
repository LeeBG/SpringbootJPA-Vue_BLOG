package com.hodolog.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hodolog.api.domain.Post;
import com.hodolog.api.repository.PostRepository;
import com.hodolog.api.request.PostCreate;
import com.hodolog.api.request.PostEdit;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.ResultMatcher;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper; // DI

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
        // given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        System.out.println(json);
        // expect
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)        // 사실 기본값이 아니였음 안쓰면 415에러conteny type지원에러
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(""))
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
        // given
        PostCreate request = PostCreate.builder()
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);
        // 글 제목, 글 내용
        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)        // 사실 기본값이 아니였음 안쓰면 415에러conteny type지원에러
                        .content(json)
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }


    @Test
    @DisplayName("/posts 요청시 title값은 필수다.")
    void test3() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        System.out.println(json);
        // 글 제목, 글 내용
        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)        // 사실 기본값이 아니였음 안쓰면 415에러conteny type지원에러
                        .content(json)
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

    @Test
    @DisplayName("글 한 개 조회")
    public void test4() throws Exception{
        // given
        // 글 저장
        Post post = Post.builder()
                .title("123456789012345")
                .content("bar")
                .build();
        postRepository.save(post);

        // expected(when, then)
        // 조회 API로 확인
        mockMvc.perform(get("/posts/{postId}",post.getId())
                        .contentType(APPLICATION_JSON)        // 사실 기본값이 아니였음 안쓰면 415에러conteny type지원에러
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("1234567890"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());

        // 클라이언트 요구사항
        // json응답에서 title값 길이를 최대 10글자로 해주세요.
        // Post Entity <-> PostResponse class


        // when
    }

    @Test
    @DisplayName("글 한 개 조회")
    public void test5() throws Exception{
        // given
        // 여러 더미 데이터 생성 및 저장
        List<Post> requestPosts = IntStream.range(0,20)
                .mapToObj(i -> Post.builder()
                        .title("호돌맨 제목" + i)
                        .content("내용" + i)
                        .build()
                ).collect(Collectors.toList());
        // sql - select, limit, offset
        postRepository.saveAll(requestPosts);

        // expected(when, then)
        // 조회 API로 확인
        mockMvc.perform(get("/posts?page=0&size=10")
                        .contentType(APPLICATION_JSON)        // 사실 기본값이 아니였음 안쓰면 415에러conteny type지원에러
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].title").value("호돌맨 제목19"))
                .andExpect(jsonPath("$[0].content").value("내용19"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 제목 수정")
    public void test7() throws Exception{
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();
        // sql - select, limit, offset
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("호돌걸")
                .content("반포자이")
                .build();

        // expected(when, then)
        // 조회 API로 확인
        mockMvc.perform(patch("/posts/{postId}", post.getId()) // PATCH /post/{postId}
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 삭제 컨트롤러")
    public void test8() throws Exception{
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();
        // sql - select, limit, offset
        postRepository.save(post);

        mockMvc.perform(delete("/posts/{postId}", post.getId()) // PATCH /post/{postId}
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    public void test9 () throws Exception{
        // expected
        mockMvc.perform(delete("/posts/{postId}",1L).contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정")
    public void test10() throws Exception{
        PostEdit postEdit = PostEdit.builder()
                .title("호돌걸")
                .content("반포자이")
                .build();

        // expected
        mockMvc.perform(patch("/posts/{postId}",1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))
                ).andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("게시긇 작성 시 '바보'는 포함 될 수 없다. 요청 시 DB에 값이 저장된다.")
    void test11() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("나는 바보입니다.")
                .content("반포자이")
                .build();

        String json = objectMapper.writeValueAsString(request);

        System.out.println(json);
        // 글 제목, 글 내용
        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)        // 사실 기본값이 아니였음 안쓰면 415에러conteny type지원에러
                        .content(json)
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}

// API 문서 생성

// GET /posts/{postId} -> 단건조회
// POST /posts -> 게시글 등록
// 자동화 문서화 툴들이 있다.
// 클라이언트 입장에서 어떤 API 있는지 모른다.

// SWAGGER나 IO DOCs도 있다.
// 우리는 RestDocs가 있다.

// 장점
// 1.운영중인 코드에 영향을 주지 않는 장점이있다. -> 테스트케이스를 통해서 문서를 생성
// 다른 도구들은 어노테이션이나 XML을 통해서 영향을 줄 수 있다.
// 2. 변경된 부분에 대해서 최신 버전의 문서가 가능하다. 코드 수정을 반영해주기 좋다.
// Test case를 실행을 해서 통과가 되면 문서를 생성해주는 방식이다.


