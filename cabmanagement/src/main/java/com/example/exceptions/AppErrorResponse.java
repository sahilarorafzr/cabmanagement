package com.example.exceptions;

import java.util.*;

public class AppErrorResponse {
    private Date timestamp;
    private String code;
    private String message;

    public AppErrorResponse(String message, String code) {
        super();
        this.timestamp = new Date();
        this.message = message;
        this.code = code;
    }
    public Date getTimestamp() {
        return timestamp;
      }
    
      public String getMessage() {
        return message;
      }
    
      public String getCode() {
        return code;
      }
}
