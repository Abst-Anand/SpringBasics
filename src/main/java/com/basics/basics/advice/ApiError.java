package com.basics.basics.advice;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


public class ApiError {

    private int statusCode;
    private String error;
    private HttpStatus statusMessage;
    private LocalDateTime timestamp;

    public ApiError(){
        timestamp = LocalDateTime.now();
    }

    public ApiError(String error, HttpStatus statusCode){
        this();
        this.statusMessage = statusCode;
        this.error = error;
        this.statusCode = statusCode.value();
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getError() {
        return error;
    }

    public HttpStatus getStatusMessage() {
        return statusMessage;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setStatusMessage(HttpStatus statusMessage) {
        this.statusMessage = statusMessage;
    }
}
