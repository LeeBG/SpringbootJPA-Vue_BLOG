package com.hodolog.api.service;

import com.hodolog.api.domain.Post;
import com.hodolog.api.exception.PostNotFound;
import com.hodolog.api.repository.PostRepository;
import com.hodolog.api.request.PostCreate;
import com.hodolog.api.request.PostEdit;
import com.hodolog.api.request.PostSearch;
import com.hodolog.api.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    public void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        // when
        postService.write(postCreate);
        // then
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test2() {
        // given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requestPost);

        // when
        PostResponse postResponse = postService.get(requestPost.getId());

        // then
        assertNotNull(postResponse);
        assertEquals(1L, postRepository.count());
        assertEquals("foo", postResponse.getTitle());
        assertEquals("bar", postResponse.getContent());
    }

    @Test
    @DisplayName("글 1페이지 조회")
    public void test3() {
        // given
        // 여러 더미 데이터 생성 및 저장
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                        .title("호돌맨 제목" + i)
                        .content("내용" + i)
                        .build()
                ).collect(Collectors.toList());
        // sql - select, limit, offset
        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(10)
                .build();

        // when
        List<PostResponse> posts = postService.getList(postSearch);

        // then
        assertEquals(10L, posts.size());
        assertEquals("호돌맨 제목19", posts.get(0).getTitle());
    }


    @Test
    @DisplayName("글 제목 수정")
    public void test4() {
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();
        // sql - select, limit, offset
        Post savedPost = postRepository.save(post);

        System.out.println("저장된 원래 게시글 : "  + savedPost);

        PostEdit postEdit = PostEdit.builder()
                .title("호돌걸")
                .content("초가집")
                .build();

        // when
        postService.edit(post.getId(),postEdit);

        // then
        Post changedPost = postRepository.findById(post.getId()).orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id="+post.getId()));
        assertEquals("호돌걸",changedPost.getTitle());
        assertEquals("초가집",changedPost.getContent());
    }

    @Test
    @DisplayName("게시글 삭제")
    public void test5() {
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();
        // sql - select, limit, offset
        Post savedPost = postRepository.save(post);

        System.out.println("저장된 원래 게시글 : "  + savedPost);

        // when
        postService.delete(post.getId());

        // then
        assertEquals(0,postRepository.count());
    }


    @Test
    @DisplayName("글 1개 조회 - 존재하지 않는 글")
    void test7() {
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();
        postRepository.save(post);

        // post.getId() // primary_id = 1

        // expected
        // 메시지에 대한 검증
        
        assertThrows(PostNotFound.class, ()->{
            postService.get(post.getId() + 1L);
        });
        
    }

    @Test
    @DisplayName("게시글 삭제 - 존재하지 않는 글")
    void test8() {
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();
        // sql - select, limit, offset
        Post savedPost = postRepository.save(post);

        // expected
        // 메시지에 대한 검증
        assertThrows(PostNotFound.class, ()->{
            postService.delete(post.getId() + 1L);
        });
    }

    @Test
    @DisplayName("글 제목 수정 - 존재하지 않는 글")
    public void test9() {
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();
        // sql - select, limit, offset
        Post savedPost = postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("호돌걸")
                .content("초가집")
                .build();

        // expected
        // 메시지에 대한 검증
        assertThrows(PostNotFound.class, ()->{
            postService.edit(post.getId() + 1L, postEdit);
        });
    }
}