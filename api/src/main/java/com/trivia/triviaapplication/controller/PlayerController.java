package com.trivia.triviaapplication.controller;

import com.trivia.triviaapplication.model.PlayerModel;
import com.trivia.triviaapplication.service.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/player/")
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
        System.out.println(userName);
        return ResponseEntity.ok(playerService.getPlayerByUserName(userName));
    }

    @PostMapping("/add")
    public ResponseEntity<PlayerModel> addNewPlayer(@RequestBody PlayerModel playerModel) {
        playerService.addNewPlayer(playerModel);
        return ResponseEntity.ok(playerModel);
    }// ToDo: test case required


    //ToDo: More requests, Error Handling and Tests
}
