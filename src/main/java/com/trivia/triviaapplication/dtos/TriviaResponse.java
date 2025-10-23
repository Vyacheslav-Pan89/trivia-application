package com.trivia.triviaapplication.dtos;

import lombok.Data;
import java.util.List;

@Data
public class TriviaResponse {
    private int response_code;
    private List<Question> results;
}
