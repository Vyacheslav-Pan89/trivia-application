package com.trivia.triviaapplication.dto;

import lombok.Data;

@Data
public class QuestionRequest {
    private Integer amount;
    private String category;
    private String difficulty;
    private String type;
}
