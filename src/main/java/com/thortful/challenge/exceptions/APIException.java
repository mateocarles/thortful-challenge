package com.thortful.challenge.exceptions;

public class APIException extends RuntimeException {
    public APIException(String message) {
        super(message);
    }
}
