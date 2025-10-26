package com.trivia.triviaapplication.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "player_data")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, updatable = false, nullable = false)
    private String name;

    @Column(name = "correct_answers")
    private Long numberOfCorrectAnswers;

    @Column(name = "wrong_answers")
    private Long numberOfWrongAnswers;

}
