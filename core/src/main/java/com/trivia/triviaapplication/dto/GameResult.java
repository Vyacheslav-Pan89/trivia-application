package com.trivia.triviaapplication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Value;

@Data
public class GameResult {

    @JsonProperty(value = "user_name")
    @NotBlank
    private String userName;

    @JsonProperty(value = "number_of_correct_answers")
    @NotNull
    @PositiveOrZero
    private Long numberOfCorrectAnswers;

    @JsonProperty(value = "number_of_wrong_answers")
    @NotNull
    @PositiveOrZero
    private Long numberOfWrongAnswers;
}
