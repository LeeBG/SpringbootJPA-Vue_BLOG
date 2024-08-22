package com.hodolog.api.controller;

import com.hodolog.api.request.PostCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// 컨트롤러 라우팅
@RestController // 쉽게 데이터 응답이 가능하다. ResponseBody가 붙은  @Controller
@Slf4j
public class PostController {
    // SSR -> jsp, thymeleaf, mustache, freemarker
    // 서버에서 렌더링을 해가지고 렌더링

    // SPA(Single Page Application) -> vue, react, nuxt, next
    // vue -> javascript에서 화면을 만들어주고 서버와의 통신은 API(JSON형태로 응답처리)

    //    @RequestMapping(method = RequestMethod.GET, path = "/vi/posts")
    @GetMapping("/test")
    public String test() {
        return "안녕";
    }


    /*
     * HTTP 요청 메소드
     * GET, POST, PUT, PATCH, DELETE, OPTIONS, HEAD, TRACE, CONNECT 등 이 있다.
     * 글 등록
     * POST Method를 이용한다.
     *
     */
    // 게시글 작성
//    @PostMapping("/posts")
//    public String post(@RequestParam String title, @RequestParam String content) {
//        log.info("title = {}, content = {}", title, content);
//        return "Hello World";
//    }

//    @PostMapping("/posts")
//    public String post(@RequestParam Map<String, String> params) {
//        log.info("params = {}",params);
//        return "Hello World";
//    }

    @PostMapping("/posts")
    public String post(@RequestBody PostCreate params) { // ModelAttribute 생략가능
        log.info("params = {}", params);
        return "Hello World";
    }
}
