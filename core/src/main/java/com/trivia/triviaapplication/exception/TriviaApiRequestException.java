package com.trivia.triviaapplication.exception;

public class TriviaApiRequestException extends TriviaApiException {

    public TriviaApiRequestException(String message) {
        super(message);
    }

    public TriviaApiRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
