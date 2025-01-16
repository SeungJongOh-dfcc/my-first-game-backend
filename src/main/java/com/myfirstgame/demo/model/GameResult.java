package com.myfirstgame.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
public class GameResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Double clearTime;

    private int coin;

    private int monstersDefeated;

    private int experienceGained;

    private int level = 1;

    private LocalDateTime createAt = LocalDateTime.now();
}
