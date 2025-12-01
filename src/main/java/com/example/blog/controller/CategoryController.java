package com.example.blog.controller;

import com.example.blog.domain.Post;
import com.example.blog.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 카테고리/태그별 포스트 필터링을 담당하는 Controller (선택 사항)
 *
 * URL:
 * - "/categories/{category}" : 특정 카테고리의 글만 보기
 * - "/tags/{tag}" : 특정 태그를 포함한 글만 보기
 *
 * 기능:
 * - 사용자가 "SPRING" 카테고리를 클릭하면 해당 글들만 필터링
 * - 사용자가 "Docker Compose" 태그를 클릭하면 해당 태그가 포함된 글들만 필터링
 */
@Controller
public class CategoryController {

    private final PostService postService;

    /**
     * 생성자 주입
     *
     * @param postService PostService 빈
     */
    public CategoryController(PostService postService) {
        this.postService = postService;
    }

    /**
     * 특정 카테고리의 포스트 목록
     *
     * URL: GET "/categories/{category}"
     * 예: "/categories/SPRING", "/categories/DOCKER"
     * View: templates/blog/index.html (홈과 동일한 레이아웃, 필터링만 다름)
     *
     * @param category URL에서 추출한 카테고리 이름
     * @param model Thymeleaf 템플릿에 데이터 전달
     * @return 템플릿 파일 경로
     *
     * 동작 순서:
     * 1. 사용자가 "/categories/SPRING" 접속
     * 2. category = "SPRING"으로 포스트 필터링
     * 3. 해당 카테고리의 포스트만 Model에 담기
     * 4. 홈과 동일한 템플릿 사용 (또는 별도 템플릿 사용 가능)
     */
    @GetMapping("/categories/{category}")
    public String postsByCategory(@PathVariable String category, Model model) {
        // 1. 해당 카테고리의 포스트만 조회
        List<Post> posts = postService.getPostsByCategory(category);

        // 2. Model에 데이터 추가
        model.addAttribute("posts", posts);
        model.addAttribute("filterType", "category"); // 템플릿에서 필터 타입 구분용
        model.addAttribute("filterValue", category); // 어떤 카테고리로 필터링했는지
        model.addAttribute("totalCount", posts.size());
        model.addAttribute("pageTitle", category + " - HOT GAMJA LAB");

        // 3. View 반환 (홈과 동일한 템플릿 재사용)
        // 또는 "blog/category" 같은 별도 템플릿 사용 가능
        return "blog/index";
    }

    /**
     * 특정 태그를 포함한 포스트 목록
     *
     * URL: GET "/tags/{tag}"
     * 예: "/tags/Docker", "/tags/Spring Boot"
     * View: templates/blog/index.html
     *
     * @param tag URL에서 추출한 태그 이름
     * @param model Thymeleaf 템플릿에 데이터 전달
     * @return 템플릿 파일 경로
     *
     * 주의:
     * - URL에 공백이 있는 태그는 URL 인코딩됩니다.
     *   예: "Spring Boot" -> "Spring%20Boot"
     * - Spring이 자동으로 디코딩해주므로 별도 처리 불필요
     */
    @GetMapping("/tags/{tag}")
    public String postsByTag(@PathVariable String tag, Model model) {
        // 1. 해당 태그를 포함한 포스트 조회
        List<Post> posts = postService.getPostsByTag(tag);

        // 2. Model에 데이터 추가
        model.addAttribute("posts", posts);
        model.addAttribute("filterType", "tag");
        model.addAttribute("filterValue", tag);
        model.addAttribute("totalCount", posts.size());
        model.addAttribute("pageTitle", "#" + tag + " - HOT GAMJA LAB");

        // 3. View 반환
        return "blog/index";
    }

    /**
     * 전체 카테고리 목록 페이지 (선택 사항)
     *
     * URL: GET "/categories"
     * 기능: 모든 카테고리를 보여주고, 각 카테고리의 포스트 개수 표시
     *
     * 예:
     * - SPRING (3개)
     * - DOCKER (2개)
     * - ALGORITHM (1개)
     * - DATABASE (1개)
     *
     * 이 기능을 구현하려면:
     * 1. Service에 "카테고리별 개수 조회" 메서드 추가
     * 2. 또는 모든 포스트를 가져와서 카테고리별로 그룹화
     */
    // @GetMapping("/categories")
    // public String categoriesList(Model model) {
    //     // 구현 예시:
    //     List<Post> allPosts = postService.getAllPosts();
    //     Map<String, Long> categoryCount = allPosts.stream()
    //         .collect(Collectors.groupingBy(Post::getCategory, Collectors.counting()));
    //
    //     model.addAttribute("categoryCount", categoryCount);
    //     model.addAttribute("pageTitle", "카테고리 - HOT GAMJA LAB");
    //     return "blog/categories-list";
    // }
}
