package com.hodolog.api.controller;

import com.hodolog.api.request.PostCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
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
    public Map<String,String> post(@RequestBody @Valid PostCreate params, BindingResult result){ // ModelAttribute 생략가능

        // 1. 메번 메서드마다 값을 검증 해야한다
        //  > 개발자가 까먹을 수 있다.
        //  > 검증 부분에서 버그가 발생할 수 있다.
        //  > 지겹다.
        // 2. 응답값에 HashMap -> 응답 클래스를 만들어 주는 것이 더 좋다.
        // 3. 여러개의 에러처리가 힘들다.
        // 4. 세 번 이상의 반복적인 작업은 피하는 것이 좋다.
        // 5. 코드 && 개발에 관한 모든 것이 포함되어 있다.
            // - 코드 && 개발에 관한 모든 것

        
        // BindingResult로 에러를 컨트롤러에서 직접 컨트롤해서 클라이언트에게 에러메시지를 전달하는 내용으로 진행
        if(result.hasErrors()){
            List<FieldError> fieldErrors = result.getFieldErrors();
            FieldError firstFieldError = fieldErrors.get(0);
            String fieldName = firstFieldError.getField();
            String errorMessage = firstFieldError.getDefaultMessage(); // ... 에러메시지(Spring이 만들어줌)

            Map<String, String> error = new HashMap<>();
            error.put(fieldName,errorMessage);
            return error;
        }

        // 위에 값 검증을 위한 @Valid를 써주면 값을 추가할 때 빈 값 혹은 null에 대한 처리를 도와준다.
        // {"title": "타이틀 값이 없습니다."}
        return Map.of();
    }
}
