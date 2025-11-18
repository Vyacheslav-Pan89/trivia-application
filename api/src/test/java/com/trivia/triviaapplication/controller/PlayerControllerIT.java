package com.trivia.triviaapplication.controller;

import com.trivia.triviaapplication.dto.GameResult;
import com.trivia.triviaapplication.model.PlayerModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PlayerControllerIT {

    private final String urlTemplate = "http://localhost:";
    @Autowired
    private TestRestTemplate testRestTemplate;
    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {

        PlayerModel playerModel1 = new PlayerModel();
        playerModel1.setUserName("Test model 1");
        playerModel1.setTotalNumberOfWrongAnswers(0L);
        playerModel1.setTotalNumberOfCorrectAnswers(0L);
        testRestTemplate.postForEntity(urlTemplate + port + "/api/player/add",
                playerModel1,
                PlayerModel.class);

        PlayerModel playerModel2 = new PlayerModel();
        playerModel2.setUserName("Test model 2");
        playerModel2.setTotalNumberOfWrongAnswers(0L);
        playerModel2.setTotalNumberOfCorrectAnswers(0L);
        testRestTemplate.postForEntity(urlTemplate + port + "/api/player/add",
                playerModel2,
                PlayerModel.class);
    }

    @Test
    void addNewPlayerShouldAddAndReturnPlayerModel() {
        PlayerModel playerModel = new PlayerModel();
        playerModel.setUserName("Test model 3");
        playerModel.setTotalNumberOfCorrectAnswers(0L);
        playerModel.setTotalNumberOfWrongAnswers(0L);

        ResponseEntity<PlayerModel> response = testRestTemplate
                .postForEntity(urlTemplate + port + "/api/player/add", playerModel, PlayerModel.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isEqualTo(playerModel);
    }

    @Test
    void addNewPlayerShouldReturnPlayerWithUserNameAlreadyExistMessage() {
        PlayerModel playerModel = new PlayerModel();
        playerModel.setUserName("Test model 1");
        playerModel.setTotalNumberOfWrongAnswers(0L);
        playerModel.setTotalNumberOfCorrectAnswers(0L);

        ResponseEntity<Map<String, String>> response = testRestTemplate
                .exchange(urlTemplate + port + "/api/player/add",
                        HttpMethod.POST,
                        new HttpEntity<>(playerModel),
                        new ParameterizedTypeReference<>() {
                        }
                );


        Map<String, String> responseBody = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseBody).isNotNull();
        if (responseBody != null) {
            assertThat(responseBody.containsKey("message")).isTrue();
            assertThat(responseBody.get("message")
                    .contains("Player with this username already exist: " + playerModel.getUserName()));
        }
    }

    @Test
    void getPlayerByUserNameShouldGetPlayerByUserName() {

        ResponseEntity<PlayerModel> response = testRestTemplate
                .exchange("/api/player/{username}",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        },
                        "Test model 1");
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        if (response.getBody() != null) {
            assertThat(response.getBody().getUserName()).isEqualTo("Test model 1");
        }
    }

    @Test
    void getPlayerByUserNameShouldReturnUserNotFoundByUserNameException() {

        ResponseEntity<Map<String, String>> response = testRestTemplate
                .exchange("/api/player/{username}",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        },
                        "Test model 5");

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();

        Map<String, String> responseBody = response.getBody();

        if (responseBody != null) {
            assertThat(responseBody.containsKey("message")).isTrue();
            assertThat(responseBody.get("message"))
                    .isEqualTo("No user found with user name: " + "Test model 5");
        }
    }

    @Test
    void getAllPlayersShouldReturnListOfPlayers() {
        ResponseEntity<List<PlayerModel>> response = testRestTemplate
                .exchange("/api/player/all",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });
        assertThat(response.hasBody()).isTrue();
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        List<PlayerModel> responseBody = response.getBody();

        if (responseBody != null) {
            assertThat(responseBody.stream()
                    .anyMatch(playerModel -> playerModel.getUserName().equals("Test model 1"))).isTrue();
            assertThat(responseBody.stream()
                    .anyMatch(playerModel -> playerModel.getUserName().equals("Test model 2"))).isTrue();
        }
    }

    @Test
    void updateUserScoreShouldReturnUpdatedPlayer() {
        GameResult gameResult = getGameResult();

        ResponseEntity<PlayerModel> response = testRestTemplate
                .exchange("/api/player",
                        HttpMethod.PUT,
                        new HttpEntity<>(gameResult),
                        new ParameterizedTypeReference<>() {
                        });

        assertThat(response.hasBody()).isTrue();
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        PlayerModel responseBody = response.getBody();

        if (responseBody != null) {
            assertThat(responseBody.getUserName()).isEqualTo("Test model 1");
            assertThat(responseBody.getTotalNumberOfCorrectAnswers()).isEqualTo(5L);
            assertThat(responseBody.getTotalNumberOfWrongAnswers()).isEqualTo(5L);
        }
    }

    @Test
    void updateUserScoreShouldReturnUserNotFoundByUserNameException() {
        GameResult gameResult = getGameResult();
        gameResult.setUserName("Test model 5");

        ResponseEntity<Map<String, String>> response = testRestTemplate
                .exchange("/api/player",
                        HttpMethod.PUT,
                        new HttpEntity<>(gameResult),
                        new ParameterizedTypeReference<>() {
                        });

        assertThat(response.hasBody()).isTrue();
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();

        Map<String, String> responseBody = response.getBody();

        if (responseBody != null) {
            assertThat(responseBody.containsKey("message")).isTrue();
            assertThat(responseBody.get("message")).isEqualTo("No user found with user name: " + "Test model 5");
        }
    }

    @Test
    void deletePlayerShouldDeleteAndReturnPlayer() {
        ResponseEntity<PlayerModel> response = testRestTemplate
                .exchange("/api/player/{username}",
                        HttpMethod.DELETE,
                        null,
                        new ParameterizedTypeReference<>() {
                        },
                        "Test model 1");

        assertThat(response.hasBody()).isTrue();
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        PlayerModel responseBody = response.getBody();

        if (responseBody != null) {
            assertThat(responseBody.getUserName()).isEqualTo("Test model 1");

            ResponseEntity<List<PlayerModel>> existingPlayers = testRestTemplate
                    .exchange("/api/player/all",
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<>() {
                            });

            if (existingPlayers.getBody() != null) {
                assertThat(existingPlayers.getBody()
                        .stream()
                        .anyMatch(playerModel -> playerModel.getUserName().equals("Test model 1")))
                        .isFalse();
            }
        }
    }

    @Test
    void deletePlayerShouldReturnUserNotFoundByUserNameException() {
        ResponseEntity<Map<String, String>> response = testRestTemplate
                .exchange("/api/player/{username}",
                        HttpMethod.DELETE,
                        null,
                        new ParameterizedTypeReference<>() {
                        },
                        "Test model 5");

        assertThat(response.hasBody()).isTrue();
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();

        Map<String, String> responseBody = response.getBody();

        if (responseBody != null) {
            assertThat(responseBody.containsKey("message")).isTrue();
            assertThat(responseBody.get("message")).isEqualTo("No user found with user name: " + "Test model 5");
        }

    }

    private GameResult getGameResult() {
        GameResult gameResult = new GameResult();
        gameResult.setUserName("Test model 1");
        gameResult.setNumberOfCorrectAnswers(5L);
        gameResult.setNumberOfWrongAnswers(5L);
        return gameResult;
    }
}
