package com.trivia.triviaapplication.service;

import com.trivia.triviaapplication.dto.Answer;
import com.trivia.triviaapplication.dto.GameResult;
import com.trivia.triviaapplication.dto.Question;
import com.trivia.triviaapplication.dto.QuestionRequest;
import com.trivia.triviaapplication.exception.GameSessionNotFoundException;
import com.trivia.triviaapplication.exception.UserNotFoundByUserNameException;
import com.trivia.triviaapplication.model.GameSession;
import com.trivia.triviaapplication.model.GameStatus;
import com.trivia.triviaapplication.model.PlayerModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {
    @Mock
    TriviaApiService triviaApiService;
    @Mock
    PlayerService playerService;
    @InjectMocks
    GameService gameService;


    @Test
    void testStartGameSuccessfully() {

        // Creating the question request for the GameService
        QuestionRequest questionRequest = createQuestionRequest();

        // Mocking the PlayerService and TriviaApiService
        when(playerService.getPlayerByUserName("Test")).thenReturn(createPlayerModel());
        when(triviaApiService.getQuestions(questionRequest)).thenReturn(createListOfQuestions());

        // Retrieving the result of the GameService
        GameSession resultGameSession = gameService
                .startGame("Test", questionRequest);

        // Assertions of the result
        assertThat(resultGameSession).isNotNull();
        assertThat(resultGameSession.getId()).isNotBlank();
        assertThat(resultGameSession.getUserName()).isEqualTo("Test");
        assertThat(resultGameSession.getStatus()).isEqualTo(GameStatus.STARTED);
        assertThat(resultGameSession.getCurrentQuestionIndex()).isEqualTo(0);
        resultGameSession.getQuestions().forEach(question
                -> assertThat(question.getCorrectAnswer()).isNotBlank());
        resultGameSession.getQuestions().forEach(question
                -> assertThat(question.getIncorrectAnswers().isEmpty()).isFalse());
        assertThat(resultGameSession.getNumberOfWrongAnswers()).isEqualTo(0L);
        assertThat(resultGameSession.getNumberOfCorrectAnswers()).isEqualTo(0L);
    }

    @Test
    void testStartGameShouldThrowUserNotFoundByUserNameException() {
        // Mocking the PlayerService
        when(playerService.getPlayerByUserName(anyString()))
                .thenThrow(new UserNotFoundByUserNameException("No user found with user name: Test"));

        // Retrieving the result of the GameService and asserting the exception
        String message = assertThrows(UserNotFoundByUserNameException.class,
                () -> gameService.startGame("Test", createQuestionRequest())).getMessage();
        // Assertions of the result message
        assertThat(message).isEqualTo("No user found with user name: Test");
    }

    @Test
    void testGetGameSession() {

        //Mocking the PlayerService and TriviaApiService
        String userName = "Test";
        QuestionRequest questionRequest = createQuestionRequest();

        when(playerService.getPlayerByUserName(userName)).thenReturn(createPlayerModel());
        when(triviaApiService.getQuestions(questionRequest)).thenReturn(createListOfQuestions());

        //Retrieving the GameSession
        GameSession expectedGameSession = gameService.startGame(userName, questionRequest);
        GameSession actualGameSession = gameService.getGameSession(expectedGameSession.getId());

        //Assertions
        assertThat(actualGameSession).isEqualTo(expectedGameSession);
        assertThat(actualGameSession.getId()).isEqualTo(expectedGameSession.getId());
        assertThat(actualGameSession.getUserName()).isEqualTo(expectedGameSession.getUserName());
        assertThat(actualGameSession.getStatus()).isEqualTo(expectedGameSession.getStatus());
        assertThat(actualGameSession.getCurrentQuestionIndex()).isEqualTo(expectedGameSession.getCurrentQuestionIndex());
    }

    @Test
    void testGetGameSessionGameSessionNotFoundException() {
        //Retrieving the GameSession and asserting the exception
        String message = assertThrows(GameSessionNotFoundException.class,
                () -> gameService.getGameSession("Test")).getMessage();
        assertThat(message).isEqualTo("No game session found with game session id: Test");
    }

    @Test
    void testSubmitCorrectAnswer() {
        //Mocking the PlayerService and TriviaApiService
        when(playerService.getPlayerByUserName("Test")).thenReturn(createPlayerModel());
        when(triviaApiService.getQuestions(createQuestionRequest())).thenReturn(createListOfQuestions());
        GameSession gameSession = gameService.startGame("Test", createQuestionRequest());

        //Retrieving the GameSession
        Answer answer = createAnswer();
        GameSession actualGameSession = gameService.submitAnswer(gameSession.getId(), answer);

        //Assertions
        assertThat(actualGameSession.getCurrentQuestionIndex()).isEqualTo(1);
        assertThat(actualGameSession.getNumberOfCorrectAnswers()).isEqualTo(1L);
        assertThat(actualGameSession.getNumberOfWrongAnswers()).isEqualTo(0L);
    }

    @Test
    void testSubmitIncorrectAnswer() {
        //Mocking the PlayerService and TriviaApiService
        when(playerService.getPlayerByUserName("Test")).thenReturn(createPlayerModel());
        when(triviaApiService.getQuestions(createQuestionRequest())).thenReturn(createListOfQuestions());
        GameSession gameSession = gameService.startGame("Test", createQuestionRequest());

        //Retrieving the GameSession
        Answer answer = createAnswer();
        answer.setAnswer("Test 1");
        GameSession actualGameSession = gameService.submitAnswer(gameSession.getId(), answer);

        //Assertions
        assertThat(actualGameSession.getCurrentQuestionIndex()).isEqualTo(1);
        assertThat(actualGameSession.getNumberOfCorrectAnswers()).isEqualTo(0L);
        assertThat(actualGameSession.getNumberOfWrongAnswers()).isEqualTo(1L);
    }

    @Test
    void testSubmitAnswerShouldDeleteFinishedGameSession() {
        //Mocking the PlayerService and TriviaApiService
        when(playerService.getPlayerByUserName("Test")).thenReturn(createPlayerModel());
        when(triviaApiService.getQuestions(createQuestionRequest())).thenReturn(createListWithSingleQuestion());

        //Retrieving the GameSession and submitting an answer
        GameSession gameSession = gameService.startGame("Test", createQuestionRequest());
        gameService.submitAnswer(gameSession.getId(), createAnswer());

        //Verifying that the PlayerService was called once
        verify((playerService), times(1)).updatePlayerScoreByGameResult(any(GameResult.class));

        //Assertions
        assertThat(gameSession.getStatus()).isEqualTo(GameStatus.FINISHED);
        assertThrows(GameSessionNotFoundException.class,
                () -> gameService.submitAnswer(gameSession.getId(), createAnswer()));
    }

    @Test
    void testSubmitAnswerShouldThrowIllegalStateException() {
        //Mocking the PlayerService and TriviaApiService
        when(playerService.getPlayerByUserName("Test")).thenReturn(createPlayerModel());
        when(triviaApiService.getQuestions(createQuestionRequest())).thenReturn(createListWithSingleQuestion());

        //Retrieving the GameSession and submitting an answer
        GameSession gameSession = gameService.startGame("Test", createQuestionRequest());
        gameSession.setStatus(GameStatus.FINISHED);

        //Assertions
        assertThrows(IllegalStateException.class,
                () -> gameService.submitAnswer(gameSession.getId(), createAnswer()));
    }

    // Helper methods
    private QuestionRequest createQuestionRequest() {
        QuestionRequest questionRequest = new QuestionRequest();
        questionRequest.setAmount(2);
        questionRequest.setCategory("General Knowledge");
        questionRequest.setDifficulty("easy");
        questionRequest.setType("multiple");
        return questionRequest;
    }

    private List<Question> createListOfQuestions() {
        Question question1 = new Question();
        question1.setCategory("General Knowledge");
        question1.setCorrectAnswer("Test");
        question1.setIncorrectAnswers(List.of("Test 1", "Test 2"));
        question1.setQuestion("Test question.");
        question1.setType("multiple");
        question1.setDifficulty("easy");

        Question question2 = new Question();
        question2.setCategory("General Knowledge");
        question2.setCorrectAnswer("Test");
        question2.setIncorrectAnswers(List.of("Test 1", "Test 2"));
        question2.setQuestion("Test question.");
        question2.setType("multiple");
        question2.setDifficulty("easy");

        return List.of(question1, question2);
    }

    private PlayerModel createPlayerModel() {
        PlayerModel playerModel = new PlayerModel();
        playerModel.setUserName("Test");
        playerModel.setTotalNumberOfCorrectAnswers(0L);
        playerModel.setTotalNumberOfWrongAnswers(0L);
        return playerModel;
    }

    private Answer createAnswer() {
        Answer answer = new Answer();
        answer.setAnswer("Test");
        return answer;
    }

    private List<Question> createListWithSingleQuestion() {
        Question question = new Question();
        question.setCategory("General Knowledge");
        question.setCorrectAnswer("Test");
        question.setIncorrectAnswers(List.of("Test 1", "Test 2"));
        question.setQuestion("Test question.");
        question.setType("multiple");
        question.setDifficulty("easy");
        return List.of(question);
    }
}
