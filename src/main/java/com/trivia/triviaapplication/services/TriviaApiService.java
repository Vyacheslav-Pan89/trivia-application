package com.trivia.triviaapplication.services;

import com.trivia.triviaapplication.dtos.CategoryResponse;
import com.trivia.triviaapplication.dtos.Question;
import com.trivia.triviaapplication.dtos.QuestionRequest;
import com.trivia.triviaapplication.dtos.TriviaResponse;
import com.trivia.triviaapplication.exceptions.TriviaApiRequestException;
import com.trivia.triviaapplication.exceptions.TriviaApiResponseException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class TriviaApiService {

    private final String apiUrl = "https://opentdb.com";
    private final RestTemplate restTemplate;

    public TriviaApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Question> getQuestions(QuestionRequest questionRequest) {

        String requestUrl = getString(questionRequest);

        try {
            TriviaResponse triviaResponse = restTemplate
                    .getForObject(requestUrl, TriviaResponse.class);
            if (triviaResponse == null || triviaResponse.getResults() == null || triviaResponse.getResults().isEmpty()) {
                throw new TriviaApiRequestException("No response or invalid response from Trivia API");
            }
            return triviaResponse.getResults();
        } catch (RestClientException e) {
            throw new TriviaApiRequestException("Failed to call Trivia API", e);
        }
    }

    public CategoryResponse getAllCategories() {
        try {
            CategoryResponse categoryResponse = restTemplate
                    .getForObject(apiUrl + "/api_category.php", CategoryResponse.class);
            if (categoryResponse == null || categoryResponse.getTriviaCategories() == null) {
                throw new TriviaApiResponseException("No response or invalid response from Trivia API");
            }
            if (categoryResponse.getTriviaCategories().isEmpty()) {
                throw new TriviaApiResponseException("Trivia API response is empty");
            }
            return categoryResponse;
        } catch (RestClientException e) {
            throw new TriviaApiRequestException("Failed to call Trivia API", e);
        }
    }

    private String getString(QuestionRequest questionRequest) {
        String requestUrl = apiUrl + "/api.php" + "?amount=" + questionRequest.getAmount();
        if (questionRequest.getCategory() != null) {
            requestUrl += "&category=" + questionRequest.getCategory();
        }
        if (questionRequest.getDifficulty() != null) {
            requestUrl += "&difficulty=" + questionRequest.getDifficulty();
        }
        if (questionRequest.getType() != null) {
            requestUrl += "&type=" + questionRequest.getType();
        }
        return requestUrl;
    }
}
