package com.trivia.triviaapplication.service;

import com.trivia.triviaapplication.dto.CategoryResponse;
import com.trivia.triviaapplication.dto.Question;
import com.trivia.triviaapplication.dto.QuestionRequest;
import com.trivia.triviaapplication.dto.TriviaResponse;
import com.trivia.triviaapplication.exception.TriviaApiRequestException;
import com.trivia.triviaapplication.exception.TriviaApiResponseException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.HtmlUtils;

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

            List<Question> results = triviaResponse.getResults();
            results.forEach(q -> {
                q.setQuestion(HtmlUtils.htmlUnescape(q.getQuestion()));
                q.setCorrectAnswer(HtmlUtils.htmlUnescape(q.getCorrectAnswer()));
                q.setIncorrectAnswers(q.getIncorrectAnswers().stream()
                        .map(HtmlUtils::htmlUnescape)
                        .toList());
            });
            return results;
        } catch (RestClientException e) {
            throw new TriviaApiRequestException("Failed to call Trivia API");
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
            throw new TriviaApiRequestException("Failed to call Trivia API");
        }
    }

    private String getString(QuestionRequest questionRequest) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl + "/api.php")
                .queryParam("amount", questionRequest.getAmount());

        if (questionRequest.getCategory() != null) {
            builder.queryParam("category", questionRequest.getCategory());
        }
        if (questionRequest.getDifficulty() != null) {
            builder.queryParam("difficulty", questionRequest.getDifficulty());
        }
        if (questionRequest.getType() != null) {
            builder.queryParam("type", questionRequest.getType());
        }

        return builder.toUriString();
    }
}
