package com.trivia.triviaapplication.services;

import com.trivia.triviaapplication.dtos.CategoryResponse;
import com.trivia.triviaapplication.dtos.Question;
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
    private final String amountTemplate = "?amount=";
    private final RestTemplate restTemplate;

    public TriviaApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Question> getQuestionsWithAmount(int amount) {
        try {
            TriviaResponse triviaResponse = restTemplate
                    .getForObject(apiUrl + "/api.php" + amountTemplate + amount, TriviaResponse.class);
            if (triviaResponse == null || triviaResponse.getResults() == null) {
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
            if(categoryResponse.getTriviaCategories().isEmpty()){
                throw new TriviaApiResponseException("Trivia API response is empty");
            }
            return categoryResponse;
        } catch (RestClientException e) {
            throw new TriviaApiRequestException("Failed to call Trivia API", e);
        }
    }
}
