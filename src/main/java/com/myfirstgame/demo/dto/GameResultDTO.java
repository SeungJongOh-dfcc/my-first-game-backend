package com.myfirstgame.demo.dto;

import com.myfirstgame.demo.model.GameResult;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameResultDTO {
    private Long id;
    private Double clearTime;
    private Integer monstersDefeated;
    private Integer coinsCollected;
    private Integer experience;
    private Integer level;

    public GameResultDTO(GameResult gameResult) {
        this.id = gameResult.getId();
        this.clearTime = gameResult.getClearTime();
        this.monstersDefeated = gameResult.getMonstersDefeated();
        this.coinsCollected = gameResult.getCoin();
        this.experience = gameResult.getExperienceGained();
        this.level = gameResult.getLevel();
    }

    // Getter, Setter...
}