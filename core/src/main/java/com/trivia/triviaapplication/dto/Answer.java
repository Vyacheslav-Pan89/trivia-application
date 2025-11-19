package com.trivia.triviaapplication.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Answer {
    @NotBlank
    private String answer;
}
