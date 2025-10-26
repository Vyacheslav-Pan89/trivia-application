package com.trivia.triviaapplication.exception;

public class UserNotFoundByUserName extends RuntimeException {
    public UserNotFoundByUserName(String message) {
        super(message);
    }
}
