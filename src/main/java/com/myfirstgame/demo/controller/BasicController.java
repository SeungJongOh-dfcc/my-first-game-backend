package com.myfirstgame.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/basic")
public class BasicController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello from spring Boot!";
    }
}
