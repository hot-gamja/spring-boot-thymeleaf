# 🚀 HOT GAMJA LAB - 다음 개발 단계

> 현재 블로그를 더 강력하고 실용적으로 만들어봅시다!

---

## 📚 목차

1. [즉시 개선 사항](#-즉시-개선-사항-1-2일)
2. [PostgreSQL 연동](#-step-1-postgresql-연동-2-3일)
3. [CRUD 관리자 페이지](#-step-2-crud-관리자-페이지-3-5일)
4. [검색 엔진](#-step-3-검색-엔진-2-3일)
5. [페이지네이션](#-step-4-페이지네이션-2-3일)
6. [댓글 시스템](#-step-5-댓글-시스템-3-5일)
7. [이미지 업로드](#-step-6-이미지-업로드-2-3일)
8. [RSS 피드](#-step-7-rss-피드-1-2일)
9. [SEO 최적화](#-step-8-seo-최적화-1-2일)
10. [배포하기](#-step-9-배포하기-3-7일)
11. [보안 강화](#-step-10-보안-강화-2-3일)

---

## 🔥 즉시 개선 사항 (1-2일)

### 1. 조회수 기능

**난이도**: ⭐ (쉬움)

#### 구현 방법

**1) Post.java에 필드 추가**
```java
public class Post {
    // 기존 필드들...
    private int viewCount;  // 조회수

    public Post(String slug, String title, /* ... */, int viewCount) {
        // ...
        this.viewCount = viewCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void incrementViewCount() {
        this.viewCount++;
    }
}
```

**2) PostService.java에 메소드 추가**
```java
@Service
public class PostService {
    // 포스트 조회 + 조회수 증가
    public Optional<Post> getPostBySlugAndIncrementView(String slug) {
        Optional<Post> postOptional = postRepository.findBySlug(slug);
        postOptional.ifPresent(Post::incrementViewCount);
        return postOptional;
    }
}
```

**3) PostController.java 수정**
```java
@GetMapping("/{slug}")
public String postDetail(@PathVariable String slug, Model model) {
    // 기존: getPostBySlug() → 변경: getPostBySlugAndIncrementView()
    Optional<Post> postOptional = postService.getPostBySlugAndIncrementView(slug);
    // ...
}
```

**4) post-detail.html에 조회수 표시**
```html
<div class="post-detail-meta">
    <time th:datetime="${post.date}"
          th:text="${#temporals.format(post.date, 'yyyy년 MM월 dd일')}">
    </time>
    <span class="view-count">
        👁️ <span th:text="${post.viewCount}">0</span>회
    </span>
</div>
```

---

### 2. 작성자(Author) 정보 추가

**난이도**: ⭐ (쉬움)

#### 구현 방법

**1) Post.java에 필드 추가**
```java
public class Post {
    // 기존 필드들...
    private String author;  // 작성자

    public Post(/* ... */, String author) {
        // ...
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }
}
```

**2) InMemoryPostRepository.java에서 더미 데이터 수정**
```java
private void initDummyData() {
    Post post1 = new Post(
        "spring-docker-setup",
        "Spring Boot와 Docker로 개발 환경 구축하기",
        // ...
        "HOT GAMJA"  // author 추가
    );
    posts.put(post1.getSlug(), post1);
}
```

**3) post-detail.html에 작성자 표시**
```html
<div class="post-author">
    <span>작성자: <strong th:text="${post.author}">HOT GAMJA</strong></span>
</div>
```

---

### 3. 읽는 시간(Reading Time) 계산

**난이도**: ⭐⭐ (중간)

#### 구현 방법

**1) Post.java에 메소드 추가**
```java
public class Post {
    /**
     * 읽는 시간 계산 (분 단위)
     * 가정: 평균 읽기 속도 = 200 단어/분
     *
     * @return 읽는 시간 (분)
     */
    public int getReadingTimeMinutes() {
        if (content == null || content.isEmpty()) {
            return 1;
        }

        // HTML 태그 제거
        String plainText = content.replaceAll("<[^>]*>", "");

        // 단어 수 계산 (공백 기준)
        int wordCount = plainText.split("\\s+").length;

        // 읽는 시간 계산 (최소 1분)
        int readingTime = Math.max(1, wordCount / 200);

        return readingTime;
    }
}
```

**2) post-detail.html에 표시**
```html
<div class="post-meta">
    <time th:datetime="${post.date}"
          th:text="${#temporals.format(post.date, 'yyyy년 MM월 dd일')}">
    </time>
    <span class="reading-time">
        📖 <span th:text="${post.readingTimeMinutes}">5</span>분
    </span>
</div>
```

---

## 🗄 STEP 1: PostgreSQL 연동 (2-3일)

**난이도**: ⭐⭐⭐ (중간)

현재는 메모리에만 저장되어 서버 재시작 시 데이터가 사라집니다. PostgreSQL로 영구 저장해봅시다!

### 1. Docker로 PostgreSQL 설치

**docker-compose.yml 생성**
```yaml
version: '3.8'

services:
  postgres:
    image: postgres:16-alpine
    container_name: blog-postgres
    environment:
      POSTGRES_DB: blog_db
      POSTGRES_USER: blog_user
      POSTGRES_PASSWORD: blog_password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  postgres_data:
```

**실행**
```bash
docker-compose up -d
```

---

### 2. Gradle 의존성 추가

**build.gradle 수정**
```gradle
dependencies {
    // 기존 의존성들...

    // PostgreSQL Driver
    implementation 'org.postgresql:postgresql'

    // Spring Data JPA
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'

    // Flyway (DB 마이그레이션)
    implementation 'org.flywaydb:flyway-core'
}
```

---

### 3. application.properties 설정

**src/main/resources/application.properties**
```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/blog_db
spring.datasource.username=blog_user
spring.datasource.password=blog_password

# JPA
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Flyway
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
```

---

### 4. Post 엔티티에 JPA 어노테이션 추가

**Post.java 수정**
```java
package com.example.blog.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity  // JPA 엔티티
@Table(name = "posts")  // 테이블 이름
public class Post {

    @Id  // Primary Key
    @Column(length = 100, nullable = false, unique = true)
    private String slug;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(columnDefinition = "TEXT")  // 긴 텍스트
    private String content;

    @Column(length = 50)
    private String category;

    @ElementCollection  // List를 별도 테이블에 저장
    @CollectionTable(name = "post_tags", joinColumns = @JoinColumn(name = "post_slug"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();

    @Column(nullable = false)
    private LocalDate date;

    @Column(length = 500)
    private String thumbnailUrl;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(nullable = false)
    private int viewCount = 0;

    @Column(length = 100)
    private String author;

    // JPA는 기본 생성자가 필수!
    protected Post() {
    }

    public Post(String slug, String title, String description, String content,
                String category, List<String> tags, LocalDate date,
                String thumbnailUrl, String summary, String author) {
        this.slug = slug;
        this.title = title;
        this.description = description;
        this.content = content;
        this.category = category;
        this.tags = tags != null ? tags : new ArrayList<>();
        this.date = date;
        this.thumbnailUrl = thumbnailUrl;
        this.summary = summary;
        this.author = author;
        this.viewCount = 0;
    }

    // Getter/Setter...
}
```

---

### 5. JpaPostRepository 생성

**JpaPostRepository.java 생성**
```java
package com.example.blog.repository;

import com.example.blog.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaPostRepository extends JpaRepository<Post, String> {

    // Spring Data JPA가 자동으로 구현해줌!

    // 모든 포스트 조회 (날짜 내림차순)
    List<Post> findAllByOrderByDateDesc();

    // slug로 조회
    Optional<Post> findBySlug(String slug);

    // 카테고리로 조회
    List<Post> findByCategoryOrderByDateDesc(String category);

    // 태그로 조회
    @Query("SELECT p FROM Post p JOIN p.tags t WHERE t = :tag ORDER BY p.date DESC")
    List<Post> findByTag(@Param("tag") String tag);

    // 카테고리로 조회 (특정 slug 제외, 개수 제한)
    @Query("SELECT p FROM Post p WHERE p.category = :category AND p.slug != :excludeSlug ORDER BY p.date DESC")
    List<Post> findByCategoryExcludingSlug(@Param("category") String category,
                                           @Param("excludeSlug") String excludeSlug);

    // 제목으로 검색
    List<Post> findByTitleContainingIgnoreCaseOrderByDateDesc(String keyword);
}
```

---

### 6. PostRepository 인터페이스 구현체 전환

**기존 InMemoryPostRepository를 사용하지 않도록 설정**

**방법 1: @Profile 사용**
```java
@Repository
@Profile("dev")  // dev 프로파일에서만 사용
public class InMemoryPostRepository implements PostRepository {
    // ...
}
```

**방법 2: PostService에서 JPA Repository 직접 사용**
```java
@Service
public class PostService {
    private final JpaPostRepository jpaPostRepository;

    public PostService(JpaPostRepository jpaPostRepository) {
        this.jpaPostRepository = jpaPostRepository;
    }

    public List<Post> getAllPosts() {
        return jpaPostRepository.findAllByOrderByDateDesc();
    }

    public Optional<Post> getPostBySlug(String slug) {
        return jpaPostRepository.findBySlug(slug);
    }

    // 조회수 증가 후 저장
    public Optional<Post> getPostBySlugAndIncrementView(String slug) {
        Optional<Post> postOptional = jpaPostRepository.findBySlug(slug);
        postOptional.ifPresent(post -> {
            post.incrementViewCount();
            jpaPostRepository.save(post);  // DB에 저장!
        });
        return postOptional;
    }

    // ...
}
```

---

### 7. Flyway 마이그레이션 스크립트 작성

**src/main/resources/db/migration/V1__create_posts_table.sql**
```sql
-- posts 테이블 생성
CREATE TABLE posts (
    slug VARCHAR(100) PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description VARCHAR(500),
    content TEXT,
    category VARCHAR(50),
    date DATE NOT NULL,
    thumbnail_url VARCHAR(500),
    summary TEXT,
    view_count INT NOT NULL DEFAULT 0,
    author VARCHAR(100)
);

-- post_tags 테이블 생성 (다대다 관계)
CREATE TABLE post_tags (
    post_slug VARCHAR(100) NOT NULL,
    tag VARCHAR(100) NOT NULL,
    FOREIGN KEY (post_slug) REFERENCES posts(slug) ON DELETE CASCADE
);

-- 인덱스 생성 (성능 향상)
CREATE INDEX idx_posts_category ON posts(category);
CREATE INDEX idx_posts_date ON posts(date DESC);
CREATE INDEX idx_post_tags_tag ON post_tags(tag);
```

**V2__insert_dummy_data.sql (더미 데이터)**
```sql
-- 더미 포스트 삽입
INSERT INTO posts (slug, title, description, content, category, date, author, view_count) VALUES
('spring-docker-setup', 'Spring Boot와 Docker로 개발 환경 구축하기',
 'Docker를 사용하여 Spring Boot 개발 환경을 구축하는 방법을 알아봅니다.',
 '<h2>개요</h2><p>Docker를 사용하면...</p>',
 'SPRING', '2025-01-15', 'HOT GAMJA', 0);

-- 태그 삽입
INSERT INTO post_tags (post_slug, tag) VALUES
('spring-docker-setup', 'Spring Boot'),
('spring-docker-setup', 'Docker');
```

---

### 8. 테스트

```bash
# 1. PostgreSQL 실행 확인
docker ps

# 2. 애플리케이션 실행
./gradlew bootRun

# 3. 로그 확인 (Flyway 마이그레이션 성공 여부)
# "Flyway: Successfully applied 2 migrations"

# 4. 브라우저에서 확인
# http://localhost:8080
```

---

## ✏️ STEP 2: CRUD 관리자 페이지 (3-5일)

**난이도**: ⭐⭐⭐⭐ (어려움)

포스트를 웹에서 직접 작성/수정/삭제할 수 있는 관리자 페이지를 만들어봅시다!

### 1. AdminController 생성

**AdminController.java**
```java
package com.example.blog.controller;

import com.example.blog.domain.Post;
import com.example.blog.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final PostService postService;

    public AdminController(PostService postService) {
        this.postService = postService;
    }

    // 관리자 홈 (포스트 목록)
    @GetMapping
    public String adminHome(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        model.addAttribute("pageTitle", "관리자 - HOT GAMJA LAB");
        return "admin/index";
    }

    // 새 포스트 작성 폼
    @GetMapping("/posts/new")
    public String newPostForm(Model model) {
        model.addAttribute("post", new Post());
        model.addAttribute("pageTitle", "새 포스트 작성 - HOT GAMJA LAB");
        return "admin/post-form";
    }

    // 포스트 수정 폼
    @GetMapping("/posts/{slug}/edit")
    public String editPostForm(@PathVariable String slug, Model model) {
        Post post = postService.getPostBySlug(slug)
            .orElseThrow(() -> new RuntimeException("포스트를 찾을 수 없습니다."));
        model.addAttribute("post", post);
        model.addAttribute("pageTitle", "포스트 수정 - HOT GAMJA LAB");
        return "admin/post-form";
    }

    // 포스트 저장 (생성 + 수정)
    @PostMapping("/posts")
    public String savePost(@ModelAttribute PostForm form) {
        // 태그 문자열을 리스트로 변환 (쉼표 구분)
        List<String> tags = Arrays.asList(form.getTagsString().split(","))
            .stream()
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .toList();

        Post post = new Post(
            form.getSlug(),
            form.getTitle(),
            form.getDescription(),
            form.getContent(),
            form.getCategory(),
            tags,
            form.getDate() != null ? form.getDate() : LocalDate.now(),
            form.getThumbnailUrl(),
            form.getSummary(),
            form.getAuthor()
        );

        postService.savePost(post);
        return "redirect:/admin";
    }

    // 포스트 삭제
    @PostMapping("/posts/{slug}/delete")
    public String deletePost(@PathVariable String slug) {
        postService.deletePost(slug);
        return "redirect:/admin";
    }
}
```

---

### 2. PostForm DTO 생성

**PostForm.java (폼 데이터 전송용)**
```java
package com.example.blog.dto;

import java.time.LocalDate;

public class PostForm {
    private String slug;
    private String title;
    private String description;
    private String content;
    private String category;
    private String tagsString;  // "Spring Boot, Docker" 같은 문자열
    private LocalDate date;
    private String thumbnailUrl;
    private String summary;
    private String author;

    // Getter/Setter 전부 생성
    // ...
}
```

---

### 3. PostService에 CRUD 메소드 추가

**PostService.java**
```java
@Service
public class PostService {
    private final JpaPostRepository jpaPostRepository;

    // 기존 메소드들...

    // 포스트 저장 (생성 + 수정)
    @Transactional
    public Post savePost(Post post) {
        return jpaPostRepository.save(post);
    }

    // 포스트 삭제
    @Transactional
    public void deletePost(String slug) {
        jpaPostRepository.deleteById(slug);
    }
}
```

---

### 4. 관리자 페이지 템플릿 생성

**admin/index.html (포스트 목록)**
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>관리자 - HOT GAMJA LAB</title>
    <link rel="stylesheet" th:href="@{/css/admin.css}">
</head>
<body>
    <div class="admin-container">
        <header class="admin-header">
            <h1>포스트 관리</h1>
            <a th:href="@{/admin/posts/new}" class="btn btn-primary">새 포스트 작성</a>
        </header>

        <table class="post-table">
            <thead>
                <tr>
                    <th>제목</th>
                    <th>카테고리</th>
                    <th>날짜</th>
                    <th>조회수</th>
                    <th>작업</th>
                </tr>
            </thead>
            <tbody>
                <tr th:each="post : ${posts}">
                    <td><a th:href="@{/posts/{slug}(slug=${post.slug})}"
                           th:text="${post.title}">제목</a></td>
                    <td th:text="${post.category}">SPRING</td>
                    <td th:text="${#temporals.format(post.date, 'yyyy.MM.dd')}">2025.01.15</td>
                    <td th:text="${post.viewCount}">0</td>
                    <td>
                        <a th:href="@{/admin/posts/{slug}/edit(slug=${post.slug})}"
                           class="btn btn-sm">수정</a>
                        <form th:action="@{/admin/posts/{slug}/delete(slug=${post.slug})}"
                              method="post" style="display:inline;">
                            <button type="submit" class="btn btn-sm btn-danger"
                                    onclick="return confirm('정말 삭제하시겠습니까?')">
                                삭제
                            </button>
                        </form>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</body>
</html>
```

**admin/post-form.html (포스트 작성/수정)**
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title th:text="${post.slug != null ? '포스트 수정' : '새 포스트 작성'}">포스트 작성</title>
    <link rel="stylesheet" th:href="@{/css/admin.css}">
</head>
<body>
    <div class="admin-container">
        <header class="admin-header">
            <h1 th:text="${post.slug != null ? '포스트 수정' : '새 포스트 작성'}">포스트 작성</h1>
        </header>

        <form th:action="@{/admin/posts}" method="post" class="post-form">
            <!-- Slug (URL) -->
            <div class="form-group">
                <label>Slug (URL)</label>
                <input type="text" name="slug" th:value="${post.slug}" required
                       placeholder="spring-docker-setup"
                       th:readonly="${post.slug != null}">
                <small>URL에 사용됩니다. 영문 소문자, 숫자, 하이픈(-)만 사용 가능</small>
            </div>

            <!-- 제목 -->
            <div class="form-group">
                <label>제목</label>
                <input type="text" name="title" th:value="${post.title}" required>
            </div>

            <!-- 설명 -->
            <div class="form-group">
                <label>설명 (미리보기)</label>
                <textarea name="description" rows="3" th:text="${post.description}"></textarea>
            </div>

            <!-- 본문 -->
            <div class="form-group">
                <label>본문 (HTML 가능)</label>
                <textarea name="content" rows="20" th:text="${post.content}"></textarea>
            </div>

            <!-- 카테고리 -->
            <div class="form-group">
                <label>카테고리</label>
                <input type="text" name="category" th:value="${post.category}" required
                       placeholder="SPRING">
            </div>

            <!-- 태그 -->
            <div class="form-group">
                <label>태그 (쉼표로 구분)</label>
                <input type="text" name="tagsString"
                       th:value="${#strings.listJoin(post.tags, ', ')}"
                       placeholder="Spring Boot, Docker">
            </div>

            <!-- 날짜 -->
            <div class="form-group">
                <label>날짜</label>
                <input type="date" name="date" th:value="${post.date}">
            </div>

            <!-- 썸네일 URL -->
            <div class="form-group">
                <label>썸네일 URL (선택)</label>
                <input type="text" name="thumbnailUrl" th:value="${post.thumbnailUrl}">
            </div>

            <!-- 요약 -->
            <div class="form-group">
                <label>요약 (선택)</label>
                <textarea name="summary" rows="3" th:text="${post.summary}"></textarea>
            </div>

            <!-- 작성자 -->
            <div class="form-group">
                <label>작성자</label>
                <input type="text" name="author" th:value="${post.author}" required>
            </div>

            <!-- 버튼 -->
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">저장</button>
                <a th:href="@{/admin}" class="btn">취소</a>
            </div>
        </form>
    </div>

    <!-- Toast UI Editor (Markdown 에디터) 추가 -->
    <script src="https://uicdn.toast.com/editor/latest/toastui-editor-all.min.js"></script>
    <link rel="stylesheet" href="https://uicdn.toast.com/editor/latest/toastui-editor.min.css" />
</body>
</html>
```

---

## 🔍 STEP 3: 검색 엔진 (2-3일)

**난이도**: ⭐⭐⭐ (중간)

### 1. SearchController 생성

**SearchController.java**
```java
package com.example.blog.controller;

import com.example.blog.domain.Post;
import com.example.blog.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {

    private final PostService postService;

    public SearchController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String q, Model model) {
        if (q == null || q.isBlank()) {
            model.addAttribute("posts", List.of());
            model.addAttribute("totalCount", 0);
            model.addAttribute("keyword", "");
        } else {
            List<Post> posts = postService.searchPosts(q);
            model.addAttribute("posts", posts);
            model.addAttribute("totalCount", posts.size());
            model.addAttribute("keyword", q);
        }

        model.addAttribute("pageTitle", "검색: " + q + " - HOT GAMJA LAB");
        return "blog/search-results";
    }
}
```

---

### 2. PostService에 검색 메소드 추가

**PostService.java**
```java
public List<Post> searchPosts(String keyword) {
    return jpaPostRepository.findByTitleContainingIgnoreCaseOrderByDateDesc(keyword);
}
```

---

### 3. 검색 폼 추가 (About 패널)

**about.html 수정**
```html
<aside th:fragment="about-panel" class="about-panel">
    <!-- 기존 내용... -->

    <!-- 검색 폼 추가 -->
    <div class="search-box">
        <form th:action="@{/search}" method="get">
            <input type="text" name="q" placeholder="포스트 검색..."
                   class="search-input" required>
            <button type="submit" class="search-btn">🔍</button>
        </form>
    </div>
</aside>
```

---

### 4. 검색 결과 페이지

**search-results.html**
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
      th:replace="~{blog/layout :: layout(~{::main})}">
<body>
    <main class="main-content">
        <div class="content-wrapper">
            <header class="page-header">
                <h2 class="page-title">
                    "<span th:text="${keyword}">검색어</span>" 검색 결과
                    <span class="count" th:text="|${totalCount}개|">0개</span>
                </h2>
            </header>

            <!-- 포스트 리스트 재사용 -->
            <div class="post-list" data-view="default">
                <article th:each="post : ${posts}" class="post-card">
                    <!-- blog/index.html과 동일한 구조 -->
                </article>
            </div>

            <!-- 결과 없음 -->
            <div th:if="${totalCount == 0}" class="empty-state">
                <p class="empty-message">
                    "<span th:text="${keyword}">검색어</span>"에 대한 검색 결과가 없습니다.
                </p>
            </div>
        </div>
    </main>
</body>
</html>
```

---

## 📄 STEP 4: 페이지네이션 (2-3일)

**난이도**: ⭐⭐⭐ (중간)

한 페이지에 모든 포스트를 보여주면 성능이 떨어집니다. 페이지네이션을 추가해봅시다!

### 1. PostService에 페이징 메소드 추가

**PostService.java**
```java
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class PostService {
    private final JpaPostRepository jpaPostRepository;

    // 페이징 조회
    public Page<Post> getAllPostsPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        return jpaPostRepository.findAll(pageable);
    }
}
```

---

### 2. BlogHomeController 수정

**BlogHomeController.java**
```java
@Controller
public class BlogHomeController {

    @GetMapping("/")
    public String home(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       Model model) {
        Page<Post> postPage = postService.getAllPostsPaged(page, size);

        model.addAttribute("posts", postPage.getContent());  // 현재 페이지 포스트
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("totalElements", postPage.getTotalElements());
        model.addAttribute("pageTitle", "HOT GAMJA LAB - 기술 블로그");

        return "blog/index";
    }
}
```

---

### 3. 페이지네이션 UI 추가

**index.html에 추가**
```html
<!-- 페이지네이션 -->
<nav class="pagination" th:if="${totalPages > 1}">
    <!-- 이전 버튼 -->
    <a th:href="@{/(page=${currentPage - 1}, size=10)}"
       class="pagination-btn"
       th:classappend="${currentPage == 0} ? 'disabled' : ''"
       th:if="${currentPage > 0}">
        ← 이전
    </a>

    <!-- 페이지 번호 -->
    <div class="pagination-numbers">
        <span th:each="i : ${#numbers.sequence(0, totalPages - 1)}"
              th:if="${i >= currentPage - 2 && i <= currentPage + 2}">
            <a th:href="@{/(page=${i}, size=10)}"
               th:text="${i + 1}"
               th:classappend="${i == currentPage} ? 'active' : ''"
               class="pagination-number">
                1
            </a>
        </span>
    </div>

    <!-- 다음 버튼 -->
    <a th:href="@{/(page=${currentPage + 1}, size=10)}"
       class="pagination-btn"
       th:classappend="${currentPage >= totalPages - 1} ? 'disabled' : ''"
       th:if="${currentPage < totalPages - 1}">
        다음 →
    </a>
</nav>
```

---

## 💬 STEP 5: 댓글 시스템 (3-5일)

**난이도**: ⭐⭐⭐⭐ (어려움)

### 1. Comment 엔티티 생성

**Comment.java**
```java
package com.example.blog.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_slug", nullable = false)
    private Post post;

    @Column(nullable = false, length = 100)
    private String author;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected Comment() {
    }

    public Comment(Post post, String author, String content) {
        this.post = post;
        this.author = author;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    // Getter/Setter...
}
```

---

### 2. CommentRepository 생성

**CommentRepository.java**
```java
package com.example.blog.repository;

import com.example.blog.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost_SlugOrderByCreatedAtAsc(String postSlug);
}
```

---

### 3. CommentService 생성

**CommentService.java**
```java
package com.example.blog.service;

import com.example.blog.domain.Comment;
import com.example.blog.domain.Post;
import com.example.blog.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getCommentsByPostSlug(String postSlug) {
        return commentRepository.findByPost_SlugOrderByCreatedAtAsc(postSlug);
    }

    @Transactional
    public Comment addComment(Post post, String author, String content) {
        Comment comment = new Comment(post, author, content);
        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
```

---

### 4. CommentController 생성

**CommentController.java**
```java
package com.example.blog.controller;

import com.example.blog.domain.Post;
import com.example.blog.service.CommentService;
import com.example.blog.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts/{slug}/comments")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    public CommentController(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    // 댓글 작성
    @PostMapping
    public String addComment(@PathVariable String slug,
                             @RequestParam String author,
                             @RequestParam String content) {
        Post post = postService.getPostBySlug(slug)
            .orElseThrow(() -> new RuntimeException("포스트를 찾을 수 없습니다."));

        commentService.addComment(post, author, content);

        return "redirect:/posts/" + slug + "#comments";
    }

    // 댓글 삭제
    @PostMapping("/{commentId}/delete")
    public String deleteComment(@PathVariable String slug,
                                @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return "redirect:/posts/" + slug + "#comments";
    }
}
```

---

### 5. 포스트 상세 페이지에 댓글 추가

**post-detail.html에 추가**
```html
<!-- 댓글 섹션 -->
<section id="comments" class="comments-section">
    <h3 class="comments-title">
        댓글 <span th:text="${#lists.size(comments)}">0</span>개
    </h3>

    <!-- 댓글 목록 -->
    <div class="comments-list">
        <article th:each="comment : ${comments}" class="comment">
            <div class="comment-header">
                <strong class="comment-author" th:text="${comment.author}">작성자</strong>
                <time class="comment-date"
                      th:datetime="${comment.createdAt}"
                      th:text="${#temporals.format(comment.createdAt, 'yyyy.MM.dd HH:mm')}">
                    2025.01.15 14:30
                </time>
            </div>
            <p class="comment-content" th:text="${comment.content}">댓글 내용</p>
        </article>
    </div>

    <!-- 댓글 작성 폼 -->
    <form th:action="@{/posts/{slug}/comments(slug=${post.slug})}"
          method="post" class="comment-form">
        <div class="form-group">
            <input type="text" name="author" placeholder="이름" required>
        </div>
        <div class="form-group">
            <textarea name="content" rows="4" placeholder="댓글을 입력하세요..." required></textarea>
        </div>
        <button type="submit" class="btn btn-primary">댓글 작성</button>
    </form>
</section>
```

---

## 📷 STEP 6: 이미지 업로드 (2-3일)

**난이도**: ⭐⭐⭐ (중간)

### 1. 파일 저장 디렉토리 설정

**application.properties**
```properties
# 파일 업로드 설정
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# 파일 저장 경로
file.upload-dir=uploads/
```

---

### 2. FileStorageService 생성

**FileStorageService.java**
```java
package com.example.blog.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 디렉토리를 생성할 수 없습니다.", e);
        }
    }

    /**
     * 파일 저장
     *
     * @param file 업로드된 파일
     * @return 저장된 파일명
     */
    public String storeFile(MultipartFile file) {
        // 원본 파일명
        String originalFilename = file.getOriginalFilename();

        // 고유한 파일명 생성 (UUID)
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFilename = UUID.randomUUID().toString() + extension;

        try {
            // 파일 저장
            Path targetLocation = this.fileStorageLocation.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return uniqueFilename;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + uniqueFilename, e);
        }
    }

    /**
     * 파일 삭제
     *
     * @param filename 파일명
     */
    public void deleteFile(String filename) {
        try {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("파일 삭제 실패: " + filename, e);
        }
    }
}
```

---

### 3. ImageController 생성

**ImageController.java**
```java
package com.example.blog.controller;

import com.example.blog.service.FileStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final FileStorageService fileStorageService;

    public ImageController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    /**
     * 이미지 업로드 API
     *
     * @param file 이미지 파일
     * @return JSON { "url": "/uploads/xxx.jpg" }
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        // 이미지 파일인지 검증
        if (!file.getContentType().startsWith("image/")) {
            return ResponseEntity.badRequest().body(Map.of("error", "이미지 파일만 업로드 가능합니다."));
        }

        // 파일 저장
        String filename = fileStorageService.storeFile(file);

        // URL 반환
        Map<String, String> response = new HashMap<>();
        response.put("url", "/uploads/" + filename);

        return ResponseEntity.ok(response);
    }
}
```

---

### 4. 정적 리소스 매핑 설정

**WebMvcConfig.java**
```java
package com.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /uploads/** URL을 실제 파일 경로로 매핑
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }
}
```

---

### 5. 관리자 폼에 이미지 업로드 추가

**post-form.html에 추가**
```html
<!-- 썸네일 이미지 업로드 -->
<div class="form-group">
    <label>썸네일 이미지</label>
    <input type="file" id="thumbnail-upload" accept="image/*">
    <input type="hidden" name="thumbnailUrl" id="thumbnail-url" th:value="${post.thumbnailUrl}">
    <div id="thumbnail-preview" th:if="${post.thumbnailUrl}">
        <img th:src="${post.thumbnailUrl}" alt="썸네일 미리보기">
    </div>
</div>

<script>
// 썸네일 업로드
document.getElementById('thumbnail-upload').addEventListener('change', async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append('file', file);

    try {
        const response = await fetch('/api/images/upload', {
            method: 'POST',
            body: formData
        });

        const data = await response.json();

        if (data.url) {
            // 썸네일 URL 설정
            document.getElementById('thumbnail-url').value = data.url;

            // 미리보기 표시
            document.getElementById('thumbnail-preview').innerHTML =
                `<img src="${data.url}" alt="썸네일 미리보기">`;
        }
    } catch (error) {
        alert('이미지 업로드 실패: ' + error.message);
    }
});
</script>
```

---

## 📡 STEP 7: RSS 피드 (1-2일)

**난이도**: ⭐⭐ (쉬움)

RSS 피드를 제공하면 사용자가 블로그를 구독할 수 있습니다!

### 1. Rome 의존성 추가

**build.gradle**
```gradle
dependencies {
    // RSS 생성 라이브러리
    implementation 'com.rometools:rome:2.1.0'
}
```

---

### 2. RssController 생성

**RssController.java**
```java
package com.example.blog.controller;

import com.example.blog.domain.Post;
import com.example.blog.service.PostService;
import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedOutput;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class RssController {

    private final PostService postService;

    public RssController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(value = "/rss", produces = "application/xml")
    public void rss(HttpServletResponse response) throws IOException, FeedException {
        // RSS 피드 생성
        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType("rss_2.0");
        feed.setTitle("HOT GAMJA LAB - 기술 블로그");
        feed.setLink("http://localhost:8080");
        feed.setDescription("Spring Boot, Docker, 알고리즘 등 개발 관련 글을 공유합니다.");

        // 포스트 목록 조회
        List<Post> posts = postService.getAllPosts();

        // RSS 아이템 생성
        List<SyndEntry> entries = new ArrayList<>();
        for (Post post : posts) {
            SyndEntry entry = new SyndEntryImpl();
            entry.setTitle(post.getTitle());
            entry.setLink("http://localhost:8080/posts/" + post.getSlug());

            SyndContent description = new SyndContentImpl();
            description.setType("text/html");
            description.setValue(post.getDescription());
            entry.setDescription(description);

            entry.setPublishedDate(Date.from(post.getDate()
                .atStartOfDay(ZoneId.systemDefault()).toInstant()));

            entries.add(entry);
        }

        feed.setEntries(entries);

        // XML 출력
        response.setContentType("application/xml; charset=UTF-8");
        SyndFeedOutput output = new SyndFeedOutput();
        output.output(feed, response.getWriter());
    }
}
```

---

### 3. RSS 링크 추가

**about.html에 추가**
```html
<div class="about-links">
    <a href="https://github.com/hot-gamja" target="_blank" class="about-link">
        GitHub
    </a>
    <a th:href="@{/rss}" target="_blank" class="about-link">
        RSS 피드
    </a>
</div>
```

---

## 🔍 STEP 8: SEO 최적화 (1-2일)

**난이도**: ⭐⭐ (쉬움)

검색 엔진 최적화를 통해 구글 검색 결과에 잘 노출되도록 합시다!

### 1. Sitemap 생성

**SitemapController.java**
```java
package com.example.blog.controller;

import com.example.blog.domain.Post;
import com.example.blog.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
public class SitemapController {

    private final PostService postService;

    public SitemapController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(value = "/sitemap.xml", produces = "application/xml")
    public void sitemap(HttpServletResponse response) throws IOException {
        response.setContentType("application/xml; charset=UTF-8");

        List<Post> posts = postService.getAllPosts();

        PrintWriter writer = response.getWriter();
        writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writer.println("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");

        // 홈페이지
        writer.println("  <url>");
        writer.println("    <loc>http://localhost:8080/</loc>");
        writer.println("    <changefreq>daily</changefreq>");
        writer.println("    <priority>1.0</priority>");
        writer.println("  </url>");

        // 각 포스트
        for (Post post : posts) {
            writer.println("  <url>");
            writer.println("    <loc>http://localhost:8080/posts/" + post.getSlug() + "</loc>");
            writer.println("    <lastmod>" + post.getDate() + "</lastmod>");
            writer.println("    <changefreq>monthly</changefreq>");
            writer.println("    <priority>0.8</priority>");
            writer.println("  </url>");
        }

        writer.println("</urlset>");
    }
}
```

---

### 2. robots.txt 생성

**src/main/resources/static/robots.txt**
```
User-agent: *
Allow: /
Sitemap: http://localhost:8080/sitemap.xml
```

---

### 3. Meta 태그 추가

**layout.html에 추가**
```html
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title th:text="${pageTitle}">HOT GAMJA LAB</title>

    <!-- SEO Meta Tags -->
    <meta name="description" th:content="${pageDescription ?: 'Spring Boot, Docker, 알고리즘 등 개발 관련 글을 공유합니다.'}">
    <meta name="keywords" content="개발, Spring Boot, Docker, 알고리즘, 기술블로그">
    <meta name="author" content="HOT GAMJA">

    <!-- Open Graph (Facebook, LinkedIn) -->
    <meta property="og:title" th:content="${pageTitle}">
    <meta property="og:description" th:content="${pageDescription ?: '기술 블로그'}">
    <meta property="og:image" th:content="${pageImage ?: '/images/og-default.jpg'}">
    <meta property="og:url" th:content="'http://localhost:8080' + ${#httpServletRequest.requestURI}">
    <meta property="og:type" content="website">

    <!-- Twitter Card -->
    <meta name="twitter:card" content="summary_large_image">
    <meta name="twitter:title" th:content="${pageTitle}">
    <meta name="twitter:description" th:content="${pageDescription ?: '기술 블로그'}">
    <meta name="twitter:image" th:content="${pageImage ?: '/images/twitter-default.jpg'}">
</head>
```

---

## 🚀 STEP 9: 배포하기 (3-7일)

**난이도**: ⭐⭐⭐⭐ (어려움)

### 방법 1: Docker로 배포

**Dockerfile 생성**
```dockerfile
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

**docker-compose.yml (프로덕션)**
```yaml
version: '3.8'

services:
  postgres:
    image: postgres:16-alpine
    container_name: blog-postgres
    environment:
      POSTGRES_DB: blog_db
      POSTGRES_USER: blog_user
      POSTGRES_PASSWORD: ${DB_PASSWORD}
    volumes:
      - postgres_data:/var/lib/postgresql/data
    restart: unless-stopped

  app:
    build: .
    container_name: blog-app
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/blog_db
      SPRING_DATASOURCE_USERNAME: blog_user
      SPRING_DATASOURCE_PASSWORD: ${DB_PASSWORD}
    ports:
      - "8080:8080"
    depends_on:
      - postgres
    restart: unless-stopped

volumes:
  postgres_data:
```

**배포**
```bash
# 1. JAR 빌드
./gradlew clean build

# 2. Docker 이미지 빌드
docker-compose build

# 3. 실행
docker-compose up -d
```

---

### 방법 2: AWS EC2 배포

**1) EC2 인스턴스 생성**
- Ubuntu Server 22.04 LTS
- t2.micro (프리티어)
- 보안 그룹: 8080 포트 오픈

**2) 서버에 Java 설치**
```bash
sudo apt update
sudo apt install openjdk-21-jre-linux-x64 -y
```

**3) PostgreSQL 설치**
```bash
sudo apt install postgresql postgresql-contrib -y
sudo -u postgres psql
```

**4) JAR 파일 전송**
```bash
scp -i your-key.pem build/libs/*.jar ubuntu@your-ec2-ip:/home/ubuntu/
```

**5) 실행**
```bash
ssh -i your-key.pem ubuntu@your-ec2-ip
java -jar app.jar
```

---

### 방법 3: GitHub Actions CI/CD

**.github/workflows/deploy.yml**
```yaml
name: Deploy to EC2

on:
  push:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v3

    - name: Set up JDK 21
      uses: actions/setup-java@v3
      with:
        java-version: '21'
        distribution: 'temurin'

    - name: Build with Gradle
      run: ./gradlew clean build -x test

    - name: Deploy to EC2
      env:
        PRIVATE_KEY: ${{ secrets.EC2_SSH_KEY }}
        HOST: ${{ secrets.EC2_HOST }}
        USER: ubuntu
      run: |
        echo "$PRIVATE_KEY" > private_key && chmod 600 private_key
        scp -i private_key build/libs/*.jar ${USER}@${HOST}:/home/ubuntu/
        ssh -i private_key ${USER}@${HOST} 'sudo systemctl restart blog-app'
```

---

## 🔒 STEP 10: 보안 강화 (2-3일)

**난이도**: ⭐⭐⭐ (중간)

### 1. Spring Security 추가

**build.gradle**
```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-security'
}
```

---

### 2. 관리자 인증 추가

**SecurityConfig.java**
```java
package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").authenticated()  // /admin/** 인증 필요
                .anyRequest().permitAll()  // 나머지는 모두 허용
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/admin", true)
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
            );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // 관리자 계정 (실제로는 DB에서 가져와야 함)
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("admin")
            .password(passwordEncoder().encode("admin123"))
            .roles("ADMIN")
            .build());
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

---

### 3. XSS 방지

**HTML 이스케이프 사용**
```html
<!-- 나쁜 예: XSS 취약점 -->
<div th:utext="${userInput}"></div>

<!-- 좋은 예: HTML 이스케이프 -->
<div th:text="${userInput}"></div>
```

---

## 🎉 마무리

### 완성된 블로그의 기능 목록

- ✅ 포스트 CRUD (관리자)
- ✅ PostgreSQL 영구 저장
- ✅ 카테고리/태그 필터링
- ✅ 검색 엔진
- ✅ 페이지네이션
- ✅ 댓글 시스템
- ✅ 이미지 업로드
- ✅ RSS 피드
- ✅ SEO 최적화 (Sitemap, Meta 태그)
- ✅ 관리자 인증 (Spring Security)
- ✅ Docker 배포
- ✅ 반응형 디자인
- ✅ 다크/라이트 테마
- ✅ 가로 스크롤 UX

### 추가로 고려할 기능

- 📊 **Google Analytics** 연동
- 📧 **이메일 구독** 기능
- 🏷️ **태그 클라우드**
- 📈 **조회수 순위**
- 💾 **포스트 임시 저장**
- 🔔 **새 글 알림**
- 🌐 **다국어 지원**
- 📱 **PWA (Progressive Web App)**

### 학습 자료

- **Spring Boot 공식 문서**: https://spring.io/projects/spring-boot
- **Spring Data JPA**: https://spring.io/projects/spring-data-jpa
- **Docker 공식 문서**: https://docs.docker.com
- **PostgreSQL 튜토리얼**: https://www.postgresql.org/docs/

---

**만든 사람**: HOT GAMJA
**문의**: 궁금한 점은 Issues에 남겨주세요!
**라이선스**: MIT License - 자유롭게 사용하세요 🚀
