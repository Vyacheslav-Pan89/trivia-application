package com.trivia.triviaapplication.controller;


import com.trivia.triviaapplication.dto.Category;
import com.trivia.triviaapplication.dto.CategoryResponse;
import com.trivia.triviaapplication.dto.Question;
import com.trivia.triviaapplication.dto.QuestionRequest;
import com.trivia.triviaapplication.exception.TriviaApiRequestException;
import com.trivia.triviaapplication.exception.TriviaApiResponseException;
import com.trivia.triviaapplication.service.TriviaApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TriviaQuestionApiController.class)
public class TriviaQuestionApiControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private TriviaApiService triviaApiService;

    @Test
    public void getQuestionsShouldReturnQuestions() throws Exception {
        Question question = getQuestion();

        QuestionRequest questionRequest = new QuestionRequest();
        questionRequest.setAmount(1);

        when(triviaApiService.getQuestions(questionRequest)).thenReturn(List.of(question));

        mockMvc.perform(get("/api/questions?amount=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].question")
                        .value("Test question."));
    }

    @Test
    public void getQuestionsShouldReturnTriviaApiRequestExceptionWhenTriviaApiFails() throws Exception {
        when(triviaApiService.getQuestions(any(QuestionRequest.class)))
                .thenThrow(new TriviaApiRequestException("Validation failed"));

        mockMvc.perform(get("/api/questions?amount=0"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message")
                        .value("Validation failed"));
    }

    @Test
    public void getQuestionsShouldReturnTriviaApiRequestExceptionWhenTriviaApiFailsWithBadUrl() throws Exception {
        when(triviaApiService.getQuestions(any(QuestionRequest.class)))
                .thenThrow(new TriviaApiRequestException("Failed to call Trivia API"));

        mockMvc.perform(get("/api/questions"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.message")
                        .value("Failed to call Trivia API"));
    }

    @Test
    public void getAllCategoriesShouldReturnListOfCategories() throws Exception {
        CategoryResponse categoryResponse = new CategoryResponse();
        Category category = new Category();
        category.setId(9);
        category.setName("Test category");
        categoryResponse.setTriviaCategories(List.of(category));

        when(triviaApiService.getAllCategories())
                .thenReturn(categoryResponse);

        mockMvc.perform(get("/api/questions/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.trivia_categories[0].id").value("9"))
                .andExpect(jsonPath("$.trivia_categories[0].name").value("Test category"));
    }

    @Test
    public void getAllCategoriesShouldReturnTriviaApiResponseExceptionWhenResponseIsInvalid() throws Exception {
        when(triviaApiService.getAllCategories())
                .thenThrow(new TriviaApiResponseException("No response or invalid response from Trivia API"));

        mockMvc.perform(get("/api/questions/categories"))
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message")
                        .value("No response or invalid response from Trivia API"));

    }

    private Question getQuestion() {
        Question question = new Question();
        question.setType("multiple");
        question.setDifficulty("easy");
        question.setQuestion("Test question.");
        question.setCategory("Test");
        return question;
    }


}
