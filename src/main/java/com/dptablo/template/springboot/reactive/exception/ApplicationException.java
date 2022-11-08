package com.dptablo.template.springboot.reactive.exception;

import lombok.Getter;

public class ApplicationException extends Exception {
    @Getter
    private final ApplicationErrorCode errorCode;

    public ApplicationException(ApplicationErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ApplicationException(ApplicationErrorCode errorCode, Throwable throwable) {
        super(throwable);
        this.errorCode = errorCode;
    }
}
