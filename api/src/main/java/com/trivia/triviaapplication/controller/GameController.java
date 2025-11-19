package com.trivia.triviaapplication.controller;

import com.trivia.triviaapplication.dto.Answer;
import com.trivia.triviaapplication.dto.QuestionRequest;
import com.trivia.triviaapplication.model.GameSession;
import com.trivia.triviaapplication.service.GameService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/start")
    public ResponseEntity<GameSession> startGame(@RequestParam(name = "username") String userName,
                                                 @Valid @RequestBody(required = false) QuestionRequest questionRequest) {
        return ResponseEntity.ok(gameService.startGame(userName, questionRequest));
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<GameSession> getGameSession(@PathVariable(name = "sessionId") @NotBlank String sessionId) {
        return ResponseEntity.ok(gameService.getGameSession(sessionId));
    }

    @PostMapping("/{sessionId}/answer")
    public ResponseEntity<GameSession> submitAnswer(@PathVariable(name = "sessionId") @NotBlank String sessionId,
                                                    @Valid @RequestBody Answer answer) {
        return ResponseEntity.ok(gameService.submitAnswer(sessionId, answer));
    }

}
