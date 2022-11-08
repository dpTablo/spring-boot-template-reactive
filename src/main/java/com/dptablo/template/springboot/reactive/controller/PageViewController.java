package com.dptablo.template.springboot.reactive.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageViewController {
    @GetMapping("/")
    public String home() {
        return "home";
    }
}
