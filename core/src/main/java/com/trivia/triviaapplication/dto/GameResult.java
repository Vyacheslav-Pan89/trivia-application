package com.trivia.triviaapplication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GameResult {
    @JsonProperty(value = "user_name")
    private String userName;
    @JsonProperty(value = "number_of_correct_answers")
    private Long numberOfCorrectAnswers;
    @JsonProperty(value = "number_of_wrong_answers")
    private Long numberOfWrongAnswers;
}
