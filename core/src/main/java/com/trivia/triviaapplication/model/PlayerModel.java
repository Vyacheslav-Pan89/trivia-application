package com.trivia.triviaapplication.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PlayerModel {

    @JsonProperty(value = "user_name")
    private String userName;
    @JsonProperty(value = "total_number_of_correct_answers")
    private Long totalNumberOfCorrectAnswers;
    @JsonProperty(value = "total_number_of_wrong_answers")
    private Long totalNumberOfWrongAnswers;
}
