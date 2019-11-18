package com.hl.experiment.exam.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExamController {

    private String hello;
    private String hello2;

    @GetMapping("/hello")
    public String getHello() {
        return hello;
    }

    @GetMapping("/hello2")
    public String getHello2() {
        return hello2;
    }

    @PutMapping("/hello")
    public String saveHello(@RequestBody String text) {
        hello = text;
        return hello;
    }

    @PostMapping("/hello2")
    public void saveHello2(@RequestParam String hello1, @RequestParam String hello2) {
        hello = hello1;
        this.hello2 = hello2;
    }
}
