package com.trivia.triviaapplication.exception;

public class PlayerWithUserNameAlreadyExistException extends RuntimeException {

    public PlayerWithUserNameAlreadyExistException(String message) {
        super(message);
    }
}
