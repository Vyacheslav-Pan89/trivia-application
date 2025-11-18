package com.trivia.triviaapplication.service;

import com.trivia.triviaapplication.dto.GameResult;
import com.trivia.triviaapplication.exception.PlayerWithUserNameAlreadyExistException;
import com.trivia.triviaapplication.exception.UserNotFoundByUserNameException;
import com.trivia.triviaapplication.model.PlayerEntity;
import com.trivia.triviaapplication.model.PlayerModel;
import com.trivia.triviaapplication.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<PlayerModel> getAllPlayers() {
        Iterable<PlayerEntity> playerEntities = playerRepository.findAll();
        List<PlayerModel> playerModelList = new ArrayList<>();
        for (PlayerEntity entity : playerEntities) {
            PlayerModel model = mapPlayerModel(entity);
            playerModelList.add(model);
        }
        return playerModelList;
    }

    public PlayerModel addNewPlayer(PlayerModel playerModel) {
        if (playerRepository.existsByUserName(playerModel.getUserName())) {
            throw new PlayerWithUserNameAlreadyExistException("Player with this username already exist: " + playerModel.getUserName());
        }
        PlayerEntity playerEntity = new PlayerEntity();
        playerEntity.setUserName(playerModel.getUserName());
        playerEntity.setTotalNumberOfCorrectAnswers(0L);
        playerEntity.setTotalNumberOfWrongAnswers(0L);
        playerRepository.save(playerEntity);
        return playerModel;
    }

    public PlayerModel getPlayerByUserName(String userName) {
        PlayerModel playerModel = new PlayerModel();
        Optional<PlayerEntity> optionalPlayerEntity = playerRepository.findByUserName(userName);
        if (optionalPlayerEntity.isEmpty()) {
            throw new UserNotFoundByUserNameException("No user found with user name: " + userName);
        }
        PlayerEntity playerEntity = optionalPlayerEntity.get();
        playerModel.setUserName(playerEntity.getUserName());
        playerModel.setTotalNumberOfWrongAnswers(playerEntity.getTotalNumberOfWrongAnswers());
        playerModel.setTotalNumberOfCorrectAnswers(playerEntity.getTotalNumberOfCorrectAnswers());
        return playerModel;
    }

    @Transactional
    public PlayerModel updatePlayerScoreByGameResult(GameResult gameResult) {
        String userName = gameResult.getUserName();
        PlayerEntity playerEntityToUpdate = getPlayerEntity(userName);
        playerEntityToUpdate
                .setTotalNumberOfWrongAnswers(playerEntityToUpdate.getTotalNumberOfWrongAnswers()
                        + gameResult.getNumberOfWrongAnswers());
        playerEntityToUpdate
                .setTotalNumberOfCorrectAnswers(playerEntityToUpdate.getTotalNumberOfCorrectAnswers()
                        + gameResult.getNumberOfCorrectAnswers());
        playerRepository.save(playerEntityToUpdate);
        return mapPlayerModel(playerEntityToUpdate);
    }

    @Transactional
    public PlayerModel deletePlayer(String userName) {
        PlayerEntity playerEntityToDelete = getPlayerEntity(userName);
        PlayerEntity playerEntity = playerRepository.deleteByUserName(playerEntityToDelete.getUserName());
        return mapPlayerModel(playerEntity);
    }

    private PlayerEntity getPlayerEntity(String userName) {
        Optional<PlayerEntity> optionalPlayerEntity = playerRepository.findByUserName(userName);
        return optionalPlayerEntity.orElseThrow(()
                -> new UserNotFoundByUserNameException("No user found with user name: " + userName));
    }

    private PlayerModel mapPlayerModel(PlayerEntity playerEntity) {
        PlayerModel playerModel = new PlayerModel();
        playerModel.setUserName(playerEntity.getUserName());
        playerModel.setTotalNumberOfCorrectAnswers(playerEntity.getTotalNumberOfCorrectAnswers());
        playerModel.setTotalNumberOfWrongAnswers(playerEntity.getTotalNumberOfWrongAnswers());
        return playerModel;
    }

}
