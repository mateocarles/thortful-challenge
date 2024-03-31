package com.thortful.challenge.exceptions;

public class JokeAlreadyStoredException extends RuntimeException {

    public JokeAlreadyStoredException(String message) {
        super(message);
    }
}
