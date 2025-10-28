package com.trivia.triviaapplication.exception;

public class UserNotFoundByUserNameException extends RuntimeException {
    public UserNotFoundByUserNameException(String message) {
        super(message);
    }
}
