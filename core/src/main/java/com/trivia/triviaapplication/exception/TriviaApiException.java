package com.trivia.triviaapplication.exception;

public class TriviaApiException extends RuntimeException {

    public TriviaApiException(String message) {
        super(message);
    }

    public TriviaApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
