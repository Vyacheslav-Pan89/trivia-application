package com.trivia.triviaapplication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TriviaApiRequestException.class)
    public ResponseEntity<String> handleTriviaApiRequestException(TriviaApiRequestException ex) {
        return new ResponseEntity<>("Failed to reach Trivia API: " + ex.getMessage(),
                HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(TriviaApiResponseException.class)
    public ResponseEntity<String> handleTriviaApiResponseException(TriviaApiResponseException ex) {
        return new ResponseEntity<>("Invalid response from Trivia API: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return new ResponseEntity<>("Unexpected error: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
