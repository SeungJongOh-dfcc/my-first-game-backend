package com.myfirstgame.demo.service;

import com.myfirstgame.demo.model.GameResult;
import com.myfirstgame.demo.model.User;
import com.myfirstgame.demo.repository.GameResultRepository;
import com.myfirstgame.demo.repository.UserRepository;
import com.myfirstgame.demo.utils.JwtUtils;
import com.myfirstgame.demo.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private GameResultRepository gameResultRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(User user) {
        if(userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }

        String encryptedPassword = PasswordUtils.encryptPassword(user.getPassword());
        user.setPassword(encryptedPassword);

        return userRepository.save(user);
    }

    public ResponseEntity<?> login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if(user != null && !user.getPassword().trim().equals(PasswordUtils.encryptPassword(password))) {
            return ResponseEntity.status(401).body("Invalid credentials");
        } else {
            // JWT 토큰 생성
            String token = JwtUtils.generateToken(username);

            // HTTP-Only 쿠키 생성
            ResponseCookie cookie = ResponseCookie.from("jwt", token)
                    .httpOnly(true)
                    //.secure(true)   // HTTPS에서만 사용
                    .path("/")
                    .maxAge(24 * 60 * 60)
                    .build();

            return ResponseEntity.ok()
                    .header("Set-Cookie", cookie.toString())
                    .body(Map.of(
                            "message", "Login successful",
                            "username", username
                    ));
        }
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    public void saveGameResult(GameResult gameResult) {
        gameResultRepository.save(gameResult);
    }

    public GameResult getGameResultByUser(User user) {
        return gameResultRepository.findByUser(user);
    }

    public String saveOrUpdateGameResult(
            String username,
            Double clearTime,
            int monstersDefeated,
            int coinsCollected,
            int experience,
            int level) {
        User user = getUser(username);

        if(user == null) {
            return "유저를 찾을 수 없습니다.";
        }

        GameResult existingResult = getGameResultByUser(user);

        if(existingResult == null) {
            // 기존 데이터가 없으면 Insert
            GameResult newResult = new GameResult();
            newResult.setUser(user);
            newResult.setClearTime(clearTime);
            newResult.setMonstersDefeated(monstersDefeated);
            newResult.setCoin(coinsCollected);
            newResult.setExperienceGained(experience);
            newResult.setLevel(level);

            saveGameResult(newResult);
        } else {
            // 기존 데이터가 있으면 업데이트
            existingResult.setClearTime(Math.min(existingResult.getClearTime(), clearTime));    // 최소시간 update
            existingResult.setMonstersDefeated(existingResult.getMonstersDefeated() + monstersDefeated);
            existingResult.setCoin(existingResult.getCoin() + coinsCollected);
            existingResult.setExperienceGained(experience);
            existingResult.setLevel(level);

            saveGameResult(existingResult);
        }

        return "게임 결과가 성공적으로 저장되었습니다!";
    }

    // 사용자 레벨과 경험치 조회
    public GameResult getUserStats(String username) {
        User user = getUser(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        GameResult gameResult = getGameResultByUser(user);
        if (gameResult == null) {
            throw new RuntimeException("Game stats not found for user");
        }

        return gameResult;
    }
}
