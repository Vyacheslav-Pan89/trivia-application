package com.trivia.triviaapplication.controller;

import com.trivia.triviaapplication.dto.CategoryResponse;
import com.trivia.triviaapplication.dto.Question;
import com.trivia.triviaapplication.dto.QuestionRequest;
import com.trivia.triviaapplication.service.TriviaApiService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class TriviaQuestionApiController {

    private final TriviaApiService triviaApiService;

    public TriviaQuestionApiController(TriviaApiService triviaApiService) {
        this.triviaApiService = triviaApiService;
    }

    @GetMapping("/categories")
    public ResponseEntity<CategoryResponse> getCategories() {
        CategoryResponse categoryResponse = triviaApiService.getAllCategories();
        return ResponseEntity.ok(categoryResponse);
    }

    @GetMapping()
    public ResponseEntity<List<Question>> getQuestions(@Valid QuestionRequest questionRequest) {
        List<Question> questions = triviaApiService.getQuestions(questionRequest);
        return ResponseEntity.ok(questions);
    }
}
