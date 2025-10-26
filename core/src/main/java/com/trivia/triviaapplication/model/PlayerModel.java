package com.trivia.triviaapplication.model;

import lombok.Data;

@Data
public class PlayerModel {

    private String name;
    private Long numberOfCorrectAnswers;
    private Long numberOfWrongAnswers;
}
