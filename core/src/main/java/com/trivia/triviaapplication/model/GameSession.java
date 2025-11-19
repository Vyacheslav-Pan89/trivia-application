package com.trivia.triviaapplication.model;

import com.trivia.triviaapplication.dto.Question;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class GameSession {

    private String id;

    @NotNull
    private String userName;
    private List<Question> questions;
    private int currentQuestionIndex;
    private Long numberOfCorrectAnswers;
    private Long numberOfWrongAnswers;

    private GameStatus status;
}
