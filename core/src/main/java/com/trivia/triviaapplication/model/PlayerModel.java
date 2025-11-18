package com.trivia.triviaapplication.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class PlayerModel {

    @JsonProperty(value = "user_name")
    @NotBlank
    private String userName;

    @JsonProperty(value = "total_number_of_correct_answers")
    @PositiveOrZero
    private Long totalNumberOfCorrectAnswers;

    @JsonProperty(value = "total_number_of_wrong_answers")
    @PositiveOrZero
    private Long totalNumberOfWrongAnswers;
}
