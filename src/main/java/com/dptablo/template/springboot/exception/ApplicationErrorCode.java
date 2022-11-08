package com.dptablo.template.springboot.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApplicationErrorCode {
    AUTHENTICATION_KEY_CREATION_FAILED(1000, "Authentication key creation failed.", HttpStatus.FORBIDDEN),
    AUTHENTICATION_EXPIRED(1001, "Authentication has expired.", HttpStatus.UNAUTHORIZED),
    AUTHENTICATION_ID_OR_PASSWORD_MISMATCH(1101, "login id or password mismatched.", HttpStatus.UNAUTHORIZED),
    SERVER_PROCESSING_ERROR(5000, "Error occurred during server processing.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int errorCode;
    private final String description;
    private final HttpStatus httpStatus;

    ApplicationErrorCode(int errorCode, String description, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
