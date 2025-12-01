package com.example.blog.controller;

import com.example.blog.domain.Post;
import com.example.blog.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

/**
 * 개별 포스트 상세 페이지를 담당하는 Controller
 *
 * URL: "/posts/{slug}"
 * 기능: 특정 포스트의 상세 내용을 보여줌 + 관련 포스트 추천
 *
 * @RequestMapping("/posts"): 이 컨트롤러의 모든 메서드는 "/posts"로 시작
 */
@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    /**
     * 생성자 주입
     *
     * @param postService PostService 빈
     */
    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * 포스트 상세 페이지
     *
     * URL: GET "/posts/{slug}"
     * 예: "/posts/spring-docker-setup"
     * View: templates/blog/post-detail.html
     *
     * @param slug URL 경로에서 추출한 포스트 식별자
     *             예: /posts/spring-docker-setup -> slug = "spring-docker-setup"
     * @param model Thymeleaf 템플릿에 데이터 전달
     * @return 템플릿 파일 경로 또는 에러 페이지
     *
     * @PathVariable: URL 경로의 {slug} 값을 메서드 파라미터로 받아옴
     *
     * 동작 순서:
     * 1. 사용자가 "/posts/spring-docker-setup" 접속
     * 2. slug = "spring-docker-setup"으로 포스트 조회
     * 3. 포스트가 있으면 상세 페이지 렌더링
     * 4. 없으면 404 에러 페이지
     */
    @GetMapping("/{slug}")
    public String postDetail(@PathVariable String slug, Model model) {
        // 1. slug로 포스트 조회
        Optional<Post> postOptional = postService.getPostBySlug(slug);

        // 2. 포스트가 없으면 404 에러 처리
        if (postOptional.isEmpty()) {
            // 방법 1: 직접 에러 페이지로 리다이렉트
            return "error/404";

            // 방법 2: Spring의 에러 처리 메커니즘 사용
            // throw new ResponseStatusException(HttpStatus.NOT_FOUND, "포스트를 찾을 수 없습니다.");
        }

        // 3. 포스트 정보를 꺼내기
        Post post = postOptional.get();

        // 4. 관련 포스트 추천 (같은 카테고리, 현재 글 제외, 최대 3개)
        List<Post> relatedPosts = postService.getRelatedPosts(
                post.getCategory(),
                post.getSlug(),
                3
        );

        // 5. Model에 데이터 추가
        model.addAttribute("post", post);
        model.addAttribute("relatedPosts", relatedPosts);
        model.addAttribute("pageTitle", post.getTitle() + " - HOT GAMJA LAB");

        // 6. View 반환
        return "blog/post-detail";
    }

    /**
     * 특정 카테고리의 포스트 목록 (선택 사항)
     *
     * URL: GET "/posts/category/{category}"
     * 예: "/posts/category/SPRING"
     *
     * 만약 URL을 "/categories/{category}"로 하고 싶다면
     * CategoryController를 별도로 만드는 게 더 깔끔합니다.
     */
    // @GetMapping("/category/{category}")
    // public String postsByCategory(@PathVariable String category, Model model) {
    //     List<Post> posts = postService.getPostsByCategory(category);
    //     model.addAttribute("posts", posts);
    //     model.addAttribute("category", category);
    //     model.addAttribute("pageTitle", category + " - HOT GAMJA LAB");
    //     return "blog/category";
    // }
}
