package com.trivia.triviaapplication.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PlayerModel {

    @JsonProperty(value = "user_name")
    private String userName;
    private Long numberOfCorrectAnswers;
    private Long numberOfWrongAnswers;
}
