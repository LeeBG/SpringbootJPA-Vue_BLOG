package com.hodolog.api.exception;

// Exception도 좋지만 UncheckedException인 RuntimerException을 상속받는다.

import lombok.NoArgsConstructor;

/**
 * status -> 404 </>
 */

public class PostNotFound extends HodologException {
    private static final String MESSAGE = "존재하지 않는 글입니다.";
    public PostNotFound() {
        super(MESSAGE);
    }

    public PostNotFound(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
