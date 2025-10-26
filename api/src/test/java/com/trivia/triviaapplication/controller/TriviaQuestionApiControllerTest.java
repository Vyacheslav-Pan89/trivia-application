package com.trivia.triviaapplication.controller;


import com.trivia.triviaapplication.dto.Question;
import com.trivia.triviaapplication.dto.QuestionRequest;
import com.trivia.triviaapplication.service.TriviaApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TriviaQuestionApiController.class)
public class TriviaQuestionApiControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private TriviaApiService triviaApiService;

    @Test
    public void shouldReturnQuestions() throws Exception {
        Question question = new Question();
        question.setType("multiple");
        question.setDifficulty("easy");
        question.setQuestion("Test question.");
        question.setCategory("Test");

        QuestionRequest questionRequest = new QuestionRequest();
        questionRequest.setAmount(1);

        when(triviaApiService.getQuestions(questionRequest)).thenReturn(List.of(question));

        mockMvc.perform(get("/api/questions?amount=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].question")
                        .value("Test question."));
    }
}
