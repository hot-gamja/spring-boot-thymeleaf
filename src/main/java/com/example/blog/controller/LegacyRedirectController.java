package com.example.blog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 기존 URL을 새 URL로 301 Redirect 처리하는 Controller
 *
 * 레거시 URL 호환성을 위해 사용합니다.
 * SEO와 북마크 호환성을 위해 301 (Moved Permanently) 사용.
 *
 * 기존 URL -> 새 URL:
 * - /posts/{slug} -> /blog/posts/{slug}
 * - /categories/{category} -> /blog/categories/{category}
 * - /tags/{tag} -> /blog/tags/{tag}
 */
@Controller
public class LegacyRedirectController {

    /**
     * /posts/{slug} -> /blog/posts/{slug}
     *
     * @param slug 포스트 식별자
     * @return 301 Redirect
     */
    @GetMapping("/posts/{slug}")
    public RedirectView redirectPost(@PathVariable String slug) {
        RedirectView redirectView = new RedirectView("/blog/posts/" + slug);
        redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        return redirectView;
    }

    /**
     * /categories/{category} -> /blog/categories/{category}
     *
     * @param category 카테고리 이름
     * @return 301 Redirect
     */
    @GetMapping("/categories/{category}")
    public RedirectView redirectCategory(@PathVariable String category) {
        RedirectView redirectView = new RedirectView("/blog/categories/" + category);
        redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        return redirectView;
    }

    /**
     * /tags/{tag} -> /blog/tags/{tag}
     *
     * @param tag 태그 이름
     * @return 301 Redirect
     */
    @GetMapping("/tags/{tag}")
    public RedirectView redirectTag(@PathVariable String tag) {
        RedirectView redirectView = new RedirectView("/blog/tags/" + tag);
        redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        return redirectView;
    }
}
