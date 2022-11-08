package com.dptablo.template.springboot.controller.advice;

import com.dptablo.template.springboot.exception.ApplicationException;
import com.dptablo.template.springboot.model.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = { "com.dptablo.template.springboot" })
@Slf4j
public class GlobalRestControllerAdvice {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ResponseDto> applicationExceptionHandler(ApplicationException e) {
        log.error(e.getMessage());
        var responseDto = ResponseDto.builder()
                .code(e.getErrorCode().getErrorCode())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseDto);
    }
}
