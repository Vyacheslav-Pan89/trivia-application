package com.trivia.triviaapplication.controller;

import com.trivia.triviaapplication.dto.CategoryResponse;
import com.trivia.triviaapplication.dto.Question;
import com.trivia.triviaapplication.dto.QuestionRequest;
import com.trivia.triviaapplication.service.TriviaApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class TriviaApiController {

    private final TriviaApiService triviaApiService;

    public TriviaApiController(TriviaApiService triviaApiService) {
        this.triviaApiService = triviaApiService;
    }

    @GetMapping("/questions/categories")
    public ResponseEntity<CategoryResponse> getCategories() {
        CategoryResponse categoryResponse = triviaApiService.getAllCategories();
        return ResponseEntity.ok(categoryResponse);
    }

    @GetMapping("/questions")
    public ResponseEntity<List<Question>> getQuestions(QuestionRequest questionRequest) {
        List<Question> questions = triviaApiService.getQuestions(questionRequest);
        return ResponseEntity.ok(questions);
    }
}
