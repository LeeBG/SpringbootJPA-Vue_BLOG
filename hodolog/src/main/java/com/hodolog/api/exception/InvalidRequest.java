package com.hodolog.api.exception;


import lombok.Getter;

/**
 * status -> 400 </>
 */
@Getter
public class InvalidRequest extends HodologException  {
    private static final String MESSAGE = "잘못된 요청입니다.";

    public InvalidRequest() {
        super(MESSAGE);
    }

    public int getStatusCode() {
        return 400;
    }

    public InvalidRequest(String message) {
        super(message);
    }

    public InvalidRequest(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName,message);
    }
}
