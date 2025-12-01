package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Home Controller
 *
 * 주의: "/" 경로는 현재 BlogHomeController에서 사용 중입니다.
 * 기존 users 페이지는 /users로 직접 접근 가능합니다.
 */
@Controller
public class HomeController {

    // "/" 경로는 BlogHomeController가 사용하도록 주석 처리
    // 기존 users 페이지는 /users로 직접 접근 가능
    /*
    @GetMapping("/")
    public String home() {
        return "redirect:/users";
    }
    */
}