package com.example.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseBody
    @ExceptionHandler(AppException.class)
    protected ResponseEntity<AppErrorResponse> handleAppException(
      RuntimeException ex, WebRequest request) {
        return new ResponseEntity<AppErrorResponse>(
            new AppErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.toString()), HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(AppExceptionNotFound.class)
    protected ResponseEntity<AppErrorResponse> handleAppExceptionNotFound(
      RuntimeException ex, WebRequest request) {
        return new ResponseEntity<AppErrorResponse>(
            new AppErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.toString()), HttpStatus.NOT_FOUND);
    }
}