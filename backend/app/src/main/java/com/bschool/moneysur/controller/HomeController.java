package com.bschool.moneysur.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HomeController {
    @GetMapping("/protected-test")
    public String home() {
        return "Si tu vois ce message, ton Cookie JWT fonctionne !";
    }
}