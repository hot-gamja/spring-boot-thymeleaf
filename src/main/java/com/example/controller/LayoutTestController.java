package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Layout Test Controller
 * 새로운 layout/layout.html 구조를 테스트하기 위한 컨트롤러
 */
@Controller
public class LayoutTestController {

    @GetMapping("/layout-test")
    public String layoutTest(Model model) {
        model.addAttribute("pageTitle", "Layout Test Page - HotGamja Lab");
        return "test/layout-test";
    }
}
