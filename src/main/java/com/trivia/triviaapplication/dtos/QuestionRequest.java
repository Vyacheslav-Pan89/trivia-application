package com.trivia.triviaapplication.dtos;

import lombok.Data;

@Data
public class QuestionRequest {
    private Integer amount;
    private String category;
    private String difficulty;
    private String type;
}
