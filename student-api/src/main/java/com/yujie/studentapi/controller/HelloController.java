package com.yujie.studentapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello Spring Boot";
    }
    @GetMapping("/name")
    public String name() {
        return "我是宇杰";
    }
    @GetMapping("/study")
    public String study() {
        return "我正在学习 Spring Boot";
    }
}