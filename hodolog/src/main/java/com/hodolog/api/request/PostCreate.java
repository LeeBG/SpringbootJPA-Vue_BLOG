package com.hodolog.api.request;

import com.hodolog.api.exception.InvalidRequest;
import lombok.*;

import javax.validation.constraints.NotBlank;

@ToString
@Setter
@Getter
@NoArgsConstructor
public class PostCreate {
    // 스프링에 빈 값 검증을 위한 어노테이션이 만들어져있다.
    // 버전에 따라 의존성을 따로 추가해야하는 경우가 있다. spring-boot-starter-validation

    // 빈값일 때 에러 메시지 커스터마이징 가능
    @NotBlank(message = "타이틀을 입력해주세요.")
    private String title;

    @NotBlank(message = "컨텐츠를 입력해주세요.")
    private String content;

    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void validate() {
        if(title.contains("바보")) {
            throw new InvalidRequest("title","제목에 바보를 포함시킬 수 없습니다.");
        }

    }
}
