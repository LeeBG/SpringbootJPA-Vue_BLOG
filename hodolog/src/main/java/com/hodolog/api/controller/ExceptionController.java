package com.hodolog.api.controller;

import com.hodolog.api.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;


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
            ErrorResponse response =  new ErrorResponse("400","잘못된 요청입니다.");
            for(FieldError fieldError : e.getFieldErrors()) {
                response.addValidation(fieldError.getField(),fieldError.getDefaultMessage());
            }
            return response;
    }
}

