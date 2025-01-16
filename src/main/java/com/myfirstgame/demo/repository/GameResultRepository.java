package com.myfirstgame.demo.repository;

import com.myfirstgame.demo.model.GameResult;
import com.myfirstgame.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameResultRepository extends JpaRepository<GameResult, Long> {
    List<GameResult> findByUserId(Long userId);

    // 특정 사용자에 해당하는 GameResult를 찾는 메서드
    GameResult findByUser(User user);
}
