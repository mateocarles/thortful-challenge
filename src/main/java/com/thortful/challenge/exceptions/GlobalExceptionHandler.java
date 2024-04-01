package com.thortful.challenge.exceptions;

import com.thortful.challenge.enums.Category;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;

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

    @ExceptionHandler(UserAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String UserAlreadyExistException(DataAccessException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleInvalidTokenException(InvalidTokenException ex) {
        return ex.getMessage();
    }
}
