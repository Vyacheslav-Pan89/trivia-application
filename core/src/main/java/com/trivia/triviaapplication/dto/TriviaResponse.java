package com.trivia.triviaapplication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TriviaResponse {
    @JsonProperty(value = "response_code")
    private int responseCode;
    private List<Question> results;
}
