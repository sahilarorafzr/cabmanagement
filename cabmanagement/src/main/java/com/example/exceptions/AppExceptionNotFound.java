package com.example.exceptions;

public class AppExceptionNotFound extends RuntimeException {

    public AppExceptionNotFound(String exception) {
      super(exception);
    }
  
}