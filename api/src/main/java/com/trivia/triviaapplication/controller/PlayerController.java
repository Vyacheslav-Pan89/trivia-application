package com.trivia.triviaapplication.controller;

import com.trivia.triviaapplication.dto.GameResult;
import com.trivia.triviaapplication.model.PlayerModel;
import com.trivia.triviaapplication.service.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<PlayerModel>> getAllPlayers() {
        return ResponseEntity.ok(playerService.getAllPlayers());
    }

    @GetMapping("/get")
    public ResponseEntity<PlayerModel> getPlayerByUserName(@RequestParam(name = "username") String userName) {
        return ResponseEntity.ok(playerService.getPlayerByUserName(userName));
    }

    @PostMapping("/add")
    public ResponseEntity<PlayerModel> addNewPlayer(@RequestBody PlayerModel playerModel) {
        return ResponseEntity.ok(playerService.addNewPlayer(playerModel));
    }// ToDo: test case required

    @PutMapping()
    public ResponseEntity<PlayerModel> updateUserScore(@RequestBody GameResult gameResult) {
        return ResponseEntity.ok(playerService.updatePlayerScoreByGameResult(gameResult));
    }//ToDo: Test cases required!

    @DeleteMapping("/delete")
    public ResponseEntity<PlayerModel> deletePlayer(@RequestParam(name = "user_name") String userName) {
        return ResponseEntity.ok(playerService.deletePlayer(userName));
    }

    //ToDo: More requests,Validation, Error Handling and Tests
}
