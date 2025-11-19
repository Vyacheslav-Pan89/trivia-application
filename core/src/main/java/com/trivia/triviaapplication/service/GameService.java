package com.trivia.triviaapplication.service;

import com.trivia.triviaapplication.dto.Answer;
import com.trivia.triviaapplication.dto.GameResult;
import com.trivia.triviaapplication.dto.Question;
import com.trivia.triviaapplication.dto.QuestionRequest;
import com.trivia.triviaapplication.exception.GameSessionNotFoundException;
import com.trivia.triviaapplication.model.GameSession;
import com.trivia.triviaapplication.model.GameStatus;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {

    private final TriviaApiService triviaApiService;
    private final PlayerService playerService;

    public GameService(TriviaApiService triviaApiService, PlayerService playerService) {
        this.triviaApiService = triviaApiService;
        this.playerService = playerService;
    }

    private final Map<String, GameSession> gameSessionMap = new ConcurrentHashMap<>();

    public GameSession startGame(String userName, QuestionRequest questionRequest) {

        playerService.getPlayerByUserName(userName);

        GameSession gameSession = new GameSession();
        gameSession.setUserName(userName);
        gameSession.setId(UUID.randomUUID().toString());
        gameSession.setNumberOfCorrectAnswers(0L);
        gameSession.setNumberOfWrongAnswers(0L);
        gameSession.setCurrentQuestionIndex(0);
        gameSession.setStatus(GameStatus.STARTED);
        gameSession.setQuestions(triviaApiService.getQuestions(questionRequest));

        gameSessionMap.put(gameSession.getId(), gameSession);
        return gameSession;
    }

    public GameSession getGameSession(String gameSessionId) {
        GameSession gameSession = gameSessionMap.get(gameSessionId);
        if (gameSession == null) {
            throw new GameSessionNotFoundException("No game session found with game session id: " + gameSessionId);
        }
        return gameSession;
    }

    public GameSession submitAnswer(String gameSessionId, Answer answer) {
        GameSession gameSession = getGameSession(gameSessionId);

        if (gameSession.getStatus() == GameStatus.FINISHED) {
            throw new IllegalStateException("Game session is already finished");
        }

        int currentQuestionIndex = gameSession.getCurrentQuestionIndex();
        Question currentQuestion = gameSession.getQuestions().get(currentQuestionIndex);

        if (currentQuestion.getCorrectAnswer().equals(answer.getAnswer())) {
            gameSession.setNumberOfCorrectAnswers(gameSession.getNumberOfCorrectAnswers() + 1);
        } else {
            gameSession.setNumberOfWrongAnswers(gameSession.getNumberOfWrongAnswers() + 1);
        }

        gameSession.setCurrentQuestionIndex(currentQuestionIndex + 1);

        if (gameSession.getCurrentQuestionIndex() >= gameSession.getQuestions().size()) {
            gameSession.setStatus(GameStatus.FINISHED);
            finishGame(gameSessionId);
            return gameSession;
        }
        gameSessionMap.put(gameSession.getId(), gameSession);
        return gameSession;
    }

    private void finishGame(String gameSessionId) {
        GameResult gameResult = new GameResult();
        gameResult.setUserName(getGameSession(gameSessionId).getUserName());
        gameResult.setNumberOfCorrectAnswers(getGameSession(gameSessionId).getNumberOfCorrectAnswers());
        gameResult.setNumberOfWrongAnswers(getGameSession(gameSessionId).getNumberOfWrongAnswers());
        playerService.updatePlayerScoreByGameResult(gameResult);
        gameSessionMap.remove(gameSessionId);
    }
}
