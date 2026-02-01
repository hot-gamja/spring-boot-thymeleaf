package com.example.blog.controller;

import com.example.blog.domain.Post;
import com.example.blog.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 블로그 홈 화면을 담당하는 Controller
 *
 * URL: "/blog"
 * 기능: 전체 포스트 목록을 보여주는 블로그 메인 페이지
 *
 * @Controller: 이 클래스가 웹 요청을 처리하는 컨트롤러임을 Spring에게 알림
 *              + View(Thymeleaf 템플릿)를 반환할 수 있음
 *
 * @RestController와의 차이:
 * - @Controller: View 이름(문자열)을 반환 -> Thymeleaf 템플릿 렌더링
 * - @RestController: JSON 데이터를 반환 -> REST API용
 */
@Controller
public class BlogHomeController {

    /**
     * PostService 주입
     *
     * Controller -> Service -> Repository 순서로 데이터를 가져옵니다.
     */
    private final PostService postService;

    /**
     * 생성자 주입
     *
     * @param postService PostService 빈
     */
    public BlogHomeController(PostService postService) {
        this.postService = postService;
    }

    /**
     * 블로그 홈 화면 (포스트 리스트)
     *
     * URL: GET "/blog"
     * View: templates/blog/index.html
     *
     * @param model Thymeleaf 템플릿에 데이터를 전달하는 객체
     * @return 템플릿 파일 경로 ("blog/index" -> templates/blog/index.html)
     *
     * 동작 순서:
     * 1. 사용자가 "/blog" 접속
     * 2. 이 메서드 실행
     * 3. PostService에서 전체 포스트 목록 조회
     * 4. Model에 데이터 담기 (Thymeleaf가 사용할 수 있게)
     * 5. "blog/index" 반환 -> Spring이 templates/blog/index.html 렌더링
     * 6. 사용자에게 HTML 응답
     */
    @GetMapping("/blog")
    public String home(Model model) {
        // 1. Service에서 전체 포스트 목록 가져오기 (최신순 정렬)
        List<Post> posts = postService.getAllPosts();

        // 2. Model에 데이터 추가
        // "posts"라는 이름으로 템플릿에서 사용 가능
        // 예: <div th:each="post : ${posts}">...</div>
        model.addAttribute("posts", posts);

        // 3. 전체 포스트 개수도 추가 (선택 사항)
        // 예: "총 5개의 글" 표시
        model.addAttribute("totalCount", posts.size());

        // 4. 페이지 제목 추가
        model.addAttribute("pageTitle", "HOT GAMJA LAB - 기술 블로그");

        // 5. View 이름 반환
        // "blog/index" -> src/main/resources/templates/blog/index.html
        return "blog/index";
    }

    /**
     * About 페이지 (선택 사항)
     *
     * URL: GET "/about"
     * View: templates/blog/about.html
     *
     * 만약 About을 별도 페이지로 만들고 싶다면 이 메서드를 활성화하세요.
     * 지금은 모든 페이지에 About 패널이 공통으로 들어가므로 생략 가능합니다.
     */
    // @GetMapping("/about")
    // public String about(Model model) {
    //     model.addAttribute("pageTitle", "About - HOT GAMJA LAB");
    //     return "blog/about";
    // }
}
