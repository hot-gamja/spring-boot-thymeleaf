package com.example.blog.controller;

import com.example.blog.domain.PostMeta;
import com.example.blog.service.MarkdownPostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class CategoryController {

    private final MarkdownPostService markdownPostService;

    public CategoryController(MarkdownPostService markdownPostService) {
        this.markdownPostService = markdownPostService;
    }

    @GetMapping("/blog/categories/{category}")
    public String postsByCategory(@PathVariable String category, Model model) {
        List<PostMeta> posts = markdownPostService.listByCategory(category);

        model.addAttribute("posts", posts);
        model.addAttribute("filterType", "category");
        model.addAttribute("filterValue", category);
        model.addAttribute("totalCount", posts.size());
        model.addAttribute("pageTitle", category + " - HOT GAMJA LAB");

        return "blog/index";
    }

    @GetMapping("/blog/tags/{tag}")
    public String postsByTag(@PathVariable String tag, Model model) {
        List<PostMeta> posts = markdownPostService.listByTag(tag);

        model.addAttribute("posts", posts);
        model.addAttribute("filterType", "tag");
        model.addAttribute("filterValue", tag);
        model.addAttribute("totalCount", posts.size());
        model.addAttribute("pageTitle", "#" + tag + " - HOT GAMJA LAB");

        return "blog/index";
    }
}
