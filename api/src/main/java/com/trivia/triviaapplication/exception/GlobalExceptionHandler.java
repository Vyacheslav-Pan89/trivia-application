package com.trivia.triviaapplication.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TriviaApiRequestException.class)
    public ResponseEntity<Map<String, Object>> handleTriviaApiRequestException(TriviaApiRequestException exception) {
        return new ResponseEntity<>(getError(exception.getMessage()), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(TriviaApiResponseException.class)
    public ResponseEntity<Map<String, Object>> handleTriviaApiResponseException(TriviaApiResponseException exception) {
        return new ResponseEntity<>(getError(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundByUserNameException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundByUserName(UserNotFoundByUserNameException exception) {
        return new ResponseEntity<>(getError(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PlayerWithUserNameAlreadyExistException.class)
    public ResponseEntity<Map<String, Object>> handlePlayerWithUserNameAlreadyExistException(PlayerWithUserNameAlreadyExistException exception) {
        return new ResponseEntity<>(getError(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolationException(ConstraintViolationException exception) {

        Map<String, Object> body = getError("Validation failed");

        Map<String, String> violations = exception.getConstraintViolations().stream().collect(Collectors.toMap(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage, (first, second) -> first));

        body.put("violations", violations);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

        Map<String, Object> body = getError("Validation failed");

        Map<String, String> fieldErrors = exception.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage, (first, second) -> first));

        body.put("fieldErrors", fieldErrors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GameSessionNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleGameSessionNotFoundException(GameSessionNotFoundException exception) {
        return new ResponseEntity<>(getError(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalStateException(IllegalStateException exception) {
        return new ResponseEntity<>(getError(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception exception) {
        Map<String, Object> body = getError("Unexpected server error");
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, Object> getError(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", Instant.now());
        error.put("message", message);
        return error;
    }
}
