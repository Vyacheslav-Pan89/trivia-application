package com.trivia.triviaapplication.repository;

import com.trivia.triviaapplication.model.PlayerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@DataJpaTest
@ContextConfiguration(classes = TestApplication.class)
public class PlayerRepositoryTest {

    @Autowired
    private PlayerRepository playerRepository;

    @BeforeEach
    void addPlayer() {
        PlayerEntity playerEntity = new PlayerEntity();
        playerEntity.setUserName("Test");
        playerEntity.setTotalNumberOfWrongAnswers(1L);
        playerEntity.setTotalNumberOfCorrectAnswers(2L);
        playerRepository.save(playerEntity);
    }

    @Test
    void shouldGenerateIdForNewPlayer() {
        PlayerEntity playerEntity = new PlayerEntity();
        playerEntity.setUserName("Name");
        playerRepository.save(playerEntity);
        assertThat(playerRepository.findByUserName("Name").get().getId()).isEqualTo(2);
    }

    @Test
    void shouldFindAndReturnNewPlayer() {
        Optional<PlayerEntity> returnedPlayerEntity = playerRepository.findByUserName("Test");
        assertThat(returnedPlayerEntity).isPresent();
        assertThat(returnedPlayerEntity.get().getUserName()).isEqualTo("Test");
    }

    @Test
    void shouldDeleteAndReturnPlayer() {
        PlayerEntity deletedPlayerEntity = playerRepository.deleteByUserName("Test");

        assertThat(deletedPlayerEntity.getUserName()).isEqualTo("Test");
        assertThat(playerRepository.findByUserName("Test")).isEmpty();
    }

    @Test
    void shouldReturnTrueWhenPlayerIsPresent() {
        assertThat(playerRepository.existsByUserName("Test")).isTrue();
    }

    @Test
    void shouldReturnFalseWhenPlayerIsNotPresent() {
        playerRepository.deleteByUserName("Test");
        assertThat(playerRepository.existsByUserName("Test")).isFalse();
    }

    @Test
    void shouldUpdateAndReturnNewPlayer() {
        PlayerEntity entityToUpdate = playerRepository.findByUserName("Test").get();
        entityToUpdate.setTotalNumberOfCorrectAnswers(10L);
        PlayerEntity updatedEntity = playerRepository.save(entityToUpdate);
        assertThat(updatedEntity.getTotalNumberOfCorrectAnswers()).isEqualTo(10L);
    }
}
