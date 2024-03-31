package com.thortful.challenge.exceptions;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JokeAlreadyStoredException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String jokeAlreadyStoredHandler(JokeAlreadyStoredException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(DrinkAlreadyStoredException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String drinkAlreadyStoredHandler(DrinkAlreadyStoredException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(APIException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String APIException(APIException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String DataAccessException(DataAccessException ex) {
        return ex.getMessage();
    }
}

