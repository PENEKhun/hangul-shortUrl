package com.penek.shortUrl.Exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
@Component
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {CustomException.class })
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e){
        log.error("CustomException : {}, {}",  e.getErrorCode(), e.getMessage());
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }


}
