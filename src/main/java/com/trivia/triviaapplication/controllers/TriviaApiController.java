package com.trivia.triviaapplication.controllers;

import com.trivia.triviaapplication.dtos.CategoryResponse;
import com.trivia.triviaapplication.dtos.Question;
import com.trivia.triviaapplication.services.TriviaApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class TriviaApiController {

    private final TriviaApiService triviaApiService;

    public TriviaApiController(TriviaApiService triviaApiService) {
        this.triviaApiService = triviaApiService;
    }

    @GetMapping("/questions/{amount}")
    public ResponseEntity<List<Question>> getQuestions(@PathVariable(name = "amount") int amount) {
        List<Question> questions = triviaApiService.getQuestionsWithAmount(amount);
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/questions/categories")
    public ResponseEntity<CategoryResponse> getCategories() {
        CategoryResponse categoryResponse = triviaApiService.getAllCategories();
        return ResponseEntity.ok(categoryResponse);
    }
}
