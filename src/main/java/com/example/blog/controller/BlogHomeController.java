package com.example.blog.controller;

import com.example.blog.domain.PostMeta;
import com.example.blog.service.MarkdownPostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BlogHomeController {

    private final MarkdownPostService markdownPostService;

    public BlogHomeController(MarkdownPostService markdownPostService) {
        this.markdownPostService = markdownPostService;
    }

    @GetMapping("/blog")
    public String home(Model model) {
        List<PostMeta> posts = markdownPostService.listPosts();

        model.addAttribute("posts", posts);
        model.addAttribute("totalCount", posts.size());
        model.addAttribute("pageTitle", "HOT GAMJA LAB - 기술 블로그");

        return "blog/index";
    }
}
