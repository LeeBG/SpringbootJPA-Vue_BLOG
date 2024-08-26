package com.hodolog.api.controller;

import com.hodolog.api.domain.Post;
import com.hodolog.api.request.PostCreate;
import com.hodolog.api.request.PostEdit;
import com.hodolog.api.request.PostSearch;
import com.hodolog.api.response.PostResponse;
import com.hodolog.api.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/*
 * HTTP 요청 메소드
 * GET, POST, PUT, PATCH, DELETE, OPTIONS, HEAD, TRACE, CONNECT 등 이 있다.
 * 글 등록
 * POST Method를 이용한다.
 *
 */

// 컨트롤러 라우팅
@RestController // 쉽게 데이터 응답이 가능하다. ResponseBody가 붙은  @Controller
@Slf4j
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    // SSR -> jsp, thymeleaf, mustache, freemarker
    // 서버에서 렌더링을 해가지고 렌더링

    // SPA(Single Page Application) -> vue, react, nuxt, next
    // vue -> javascript에서 화면을 만들어주고 서버와의 통신은 API(JSON형태로 응답처리)

    @GetMapping("/test")
    public String test() {
        return "안녕";
    }


    // 글 작성
    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request){ // ModelAttribute 생략가능
        // Case1. 저장한 데이터 Entity -> Response로 응답하기
        postService.write(request);
        
        // Case2. 저장한 데이터 primary_id -> response로 응답하기
        // Client에서는 수신한 id를 post조회 API를 통해서 글 데이터를 수신받음
//        Long postId = postService.write(request);
//        return Map.of("postId", postId)
        
        // Case3. 응답 필요없음 -> Client에서 모든 Post(글) 데이터 context들을 잘 관리함
        // Bad Case : 서버에서 -> 반드시 이렇게 할것입니다. fix
        //      -> 서버에서 처라리 유연하게 대응하는 것이 좋습니다.
        //      -> 한 번에 일괄적으로 잘 처리되는 case는 없다. -> 잘 관리하는 형태가 중요하다.
    }

    // 글 조회
    /**
     *  /posts -< 글 전체 조회(검색 + 페이징)
     *  /posts/{postId} -> 글 한개만 조회
     */

    // 단건 조회 API
    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable(name = "postId") Long id){
        return postService.get(id);
    }

    // 조회용 API
    // 여러개의 글을 조회하는 API - 리스트 가져오기
    // /posts
    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit request) {
        postService.edit(postId,request);
    }
}
