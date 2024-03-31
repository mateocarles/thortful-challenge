package com.thortful.challenge.exceptions;

public class DrinkAlreadyStoredException extends RuntimeException {

    public DrinkAlreadyStoredException(String message) {
        super(message);
    }
}

