package com.hodolog.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * {
 *      "code" : "400",
 *      "message" : "잘못된 요청입니다.",
 *      "validation" : {
 *          "title" : "값을 입력해주세요"
 *      }
 * }
 */

@Getter
@JsonInclude(value = JsonInclude.Include.NON_EMPTY) // 비어있지 않은 내용만 포함
public class ErrorResponse {
    // 회사마다 팀마다 규칙이 조금씩 다르다.
    private final String code;
    private final String message;
    private final Map<String, String> validation = new HashMap<>();

    @Builder
    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName,errorMessage);
    }


}
