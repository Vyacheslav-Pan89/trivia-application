package com.trivia.triviaapplication.exception;

public class TriviaApiResponseException extends TriviaApiException {
    public TriviaApiResponseException(String message) {
        super(message);
    }

    public TriviaApiResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}
