package com.myfirstgame.demo.controller;

import com.myfirstgame.demo.dto.GameResultDTO;
import com.myfirstgame.demo.model.GameResult;
import com.myfirstgame.demo.model.User;
import com.myfirstgame.demo.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        return userService.login(user.getUsername(), user.getPassword());
    }

    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        return userService.getUser(username);
    }

    @PostMapping("/game-results")
    public ResponseEntity<?> saveOrUpdateGameResult(@RequestBody Map<String, Object> payload) {
        String username = (String) payload.get("username");
        double newClearTime = (Double) payload.get("clearTime");
        int newMonstersDefeated = (int) payload.get("monstersDefeated");
        int newCoinsCollected = (int) payload.get("coinsCollected");
        int experience = (int) payload.get("experience");
        int level = (int) payload.get("level");

        String result = userService.saveOrUpdateGameResult(
                username,
                newClearTime,
                newMonstersDefeated,
                newCoinsCollected,
                experience,
                level
        );

        if ("유저를 찾을 수 없습니다.".equals(result)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getUserStats(@RequestParam("username") String username) {
        try {
            GameResult userStats = userService.getUserStats(username);
            GameResultDTO gameResultDTO = new GameResultDTO(userStats);
            return ResponseEntity.ok(gameResultDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body(e.getMessage());
        }
    }
}