package com.hodolog.api.controller;

import com.hodolog.api.exception.HodologException;
import com.hodolog.api.exception.InvalidRequest;
import com.hodolog.api.exception.PostNotFound;
import com.hodolog.api.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


/*
    @ResponseBody
    MessageConverter가 모델을 뷰로 랜더링하는 것이 아니라 반환된 객체를 응답 본문에 쓰이도록 지시한다.

 */
@ControllerAdvice
@Slf4j
public class ExceptionController {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorResponse InvalidRequestHandler(MethodArgumentNotValidException e) {
            ErrorResponse response =  ErrorResponse.builder()
                    .code("400")
                    .message("잘못된 요청입니다.")
                    .build();

            for(FieldError fieldError : e.getFieldErrors()) {
                response.addValidation(fieldError.getField(),fieldError.getDefaultMessage());
            }
            return response;
    }

    @ResponseBody
    @ExceptionHandler(HodologException.class)
    public ResponseEntity<ErrorResponse> hodologException(HodologException e){
        int statusCode = e.getStatusCode();

        ErrorResponse body =  ErrorResponse.builder()
                .code(String.valueOf(e.getStatusCode()))
                .message(e.getMessage())
                .build();

        if(e instanceof InvalidRequest){
            InvalidRequest invalidRequest = (InvalidRequest) e;
            String fieldName = invalidRequest.getFieldName();
            String message = invalidRequest.getMessage();
            body.addValidation(fieldName,message);
        }

        ResponseEntity<ErrorResponse> response = ResponseEntity.status(statusCode)
                .body(body);

        return response; // ResponseEntity객체 자체가 알아서 생성해줌 status와 body 같은 것들
    }
}

