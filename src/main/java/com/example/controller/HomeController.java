package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Home Controller
 *
 * 메인 홈 페이지를 담당합니다.
 * URL: "/"
 */
@Controller
public class HomeController {

    /**
     * 메인 홈 페이지
     *
     * URL: GET "/"
     * View: templates/home.html
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pageTitle", "HotGamja Lab - Home");
        return "home";
    }
}
