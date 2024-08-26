package com.hodolog.api.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@ToString
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

//    public String getTitle() {
//        // 서비스의 정책을 절대 넣지 마세요!!
//        return this.title.substring(0, 10);
//    }

    public void change(String title, String content){
        this.title = title != null ? title : this.title;
        this.content = content!= null ? title : this.title;
    }

    // Builder 클래스 자체를 넘겨준다.
    public PostEditor.PostEditorBuilder toEditor() {
        return PostEditor.builder()
                .title(title)
                .content(content);
    }

    // PostEditor가 우리가 수정이 필요한 애들만 가지고 있다.
    public void edit(PostEditor postEditor){
        title = postEditor.getTitle();
        content = postEditor.getContent();
    }
}
