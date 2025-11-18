package com.trivia.triviaapplication.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class QuestionRequest {

    @Positive
    @Max(value = 20)
    private Integer amount;
    private String category;
    private String difficulty;
    private String type;
}
