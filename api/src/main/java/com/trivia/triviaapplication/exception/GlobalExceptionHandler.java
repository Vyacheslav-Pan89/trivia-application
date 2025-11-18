package com.trivia.triviaapplication.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TriviaApiRequestException.class)
    public ResponseEntity<Map<String, String>> handleTriviaApiRequestException(TriviaApiRequestException exception) {
        return new ResponseEntity<>(getError(exception.getMessage()), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(TriviaApiResponseException.class)
    public ResponseEntity<Map<String, String>> handleTriviaApiResponseException(TriviaApiResponseException exception) {
        return new ResponseEntity<>(getError(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundByUserNameException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundByUserName(UserNotFoundByUserNameException exception) {
        return new ResponseEntity<>(getError(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PlayerWithUserNameAlreadyExistException.class)
    public ResponseEntity<Map<String, String>> handlePlayerWithUserNameAlreadyExistException(
            PlayerWithUserNameAlreadyExistException exception) {
        return new ResponseEntity<>(getError(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(
            ConstraintViolationException exception) {
        return new ResponseEntity<>(getError(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception exception) {
        return new ResponseEntity<>(getError(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, String> getError(String exception) {
        Map<String, String> error = new HashMap<>();
        error.put("error", exception);
        return error;
    }
}
