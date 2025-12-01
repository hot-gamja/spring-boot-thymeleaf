# 🎓 HOT GAMJA LAB - 기술 블로그 학습 가이드

> dayloog.com 스타일의 기술 블로그를 만들면서 Spring Boot, Thymeleaf, Vanilla JS를 배워봅시다!

---

## 📚 목차

1. [프로젝트 개요](#-프로젝트-개요)
2. [기술 스택](#-기술-스택)
3. [프로젝트 구조](#-프로젝트-구조)
4. [아키텍처 이해하기](#-아키텍처-이해하기)
5. [레이어별 학습 포인트](#-레이어별-학습-포인트)
6. [주요 기능 코드 분석](#-주요-기능-코드-분석)
7. [학습 순서 추천](#-학습-순서-추천)
8. [실습 방법](#-실습-방법)
9. [문제 해결 가이드](#-문제-해결-가이드)

---

## 🎯 프로젝트 개요

### 무엇을 만들었나?

**dayloog.com** 스타일의 미니멀한 기술 블로그입니다.

#### 주요 특징
- 🎨 **다크/라이트 테마** 지원
- 📱 **완벽한 반응형** 디자인 (모바일/태블릿/PC)
- 🖱️ **가로 스크롤** 포스트 리스트 (마우스 휠 + 드래그)
- 🔤 **폰트 크기 조절** (S/M/L)
- 🖼️ **뷰 모드 전환** (Default/Image Grid)
- 💾 **설정 저장** (localStorage 사용)
- 📂 **카테고리/태그** 필터링
- 🔗 **SEO 친화적** URL 구조

### 왜 이 프로젝트인가?

이 프로젝트는 다음을 배우기에 최적화되어 있습니다:

1. **백엔드 기초**: Spring Boot의 레이어드 아키텍처
2. **프론트엔드 기초**: HTML/CSS/JS 없이 프레임워크 없는 순수 개발
3. **템플릿 엔진**: Thymeleaf로 서버 사이드 렌더링
4. **디자인 패턴**: Repository Pattern, Dependency Injection
5. **실무 스킬**: 반응형 디자인, 사용자 경험 최적화

---

## 🛠 기술 스택

### Backend
- **Spring Boot 3.5.7** - 웹 애플리케이션 프레임워크
- **Java 21** - 프로그래밍 언어
- **Gradle 8.12** - 빌드 도구

### Frontend
- **Thymeleaf** - 서버 사이드 템플릿 엔진
- **Vanilla JavaScript** - 순수 JS (프레임워크 없음)
- **CSS3** - 스타일링 (Grid, Flexbox, Variables)

### Data
- **In-Memory Storage** - LinkedHashMap 사용 (나중에 PostgreSQL로 쉽게 전환 가능)

---

## 📁 프로젝트 구조

```
spring-boot-thymeleaf/
│
├── src/main/java/com/example/
│   ├── blog/
│   │   ├── controller/
│   │   │   ├── BlogHomeController.java      # GET / (블로그 홈)
│   │   │   ├── PostController.java          # GET /posts/{slug} (포스트 상세)
│   │   │   └── CategoryController.java      # GET /categories/{category}, /tags/{tag}
│   │   ├── service/
│   │   │   └── PostService.java             # 비즈니스 로직
│   │   ├── repository/
│   │   │   ├── PostRepository.java          # 인터페이스 (추상화)
│   │   │   └── InMemoryPostRepository.java  # 구현체 (메모리 저장소)
│   │   └── domain/
│   │       └── Post.java                    # 도메인 모델 (엔티티)
│   └── controller/
│       └── HomeController.java              # 기존 컨트롤러 (수정됨)
│
├── src/main/resources/
│   ├── templates/
│   │   ├── blog/
│   │   │   ├── layout.html                  # 베이스 레이아웃
│   │   │   ├── index.html                   # 포스트 리스트 페이지
│   │   │   └── post-detail.html             # 포스트 상세 페이지
│   │   ├── blog-fragments/
│   │   │   ├── about.html                   # About 패널 (프로필)
│   │   │   ├── brand-background.html        # 브랜드 배경 (HOT GAMJA LAB 텍스트)
│   │   │   └── reader-controls.html         # Reader Controls (설정 패널)
│   │   └── error/
│   │       └── 404.html                     # 404 에러 페이지
│   └── static/
│       ├── css/
│       │   ├── blog-base.css                # 기본 스타일 (변수, 리셋, 타이포그래피)
│       │   ├── blog-layout.css              # 레이아웃 (About, Brand, Main)
│       │   ├── blog-post.css                # 포스트 카드 & 상세 페이지
│       │   └── blog-responsive.css          # 반응형 (5개 브레이크포인트)
│       └── js/
│           ├── reader-controls.js           # 설정 컨트롤 (폰트/테마/뷰)
│           └── scroll-horizontal.js         # 가로 스크롤 (휠 + 드래그)
│
├── build.gradle                             # Gradle 설정
└── LEARNING_GUIDE.md                        # 이 문서!
```

---

## 🏗 아키텍처 이해하기

### 1. 레이어드 아키텍처 (Layered Architecture)

```
┌─────────────────────────────────────┐
│         Presentation Layer          │  ← Controller (HTTP 요청 처리)
│   BlogHomeController, PostController│
└─────────────────┬───────────────────┘
                  │
┌─────────────────▼───────────────────┐
│         Service Layer               │  ← Business Logic
│         PostService                 │
└─────────────────┬───────────────────┘
                  │
┌─────────────────▼───────────────────┐
│         Repository Layer            │  ← Data Access
│   PostRepository (Interface)        │
│   InMemoryPostRepository (Impl)     │
└─────────────────┬───────────────────┘
                  │
┌─────────────────▼───────────────────┐
│         Domain Layer                │  ← Entity (비즈니스 객체)
│         Post.java                   │
└─────────────────────────────────────┘
```

### 2. 의존성 흐름 (Dependency Flow)

- **Controller** → Service → Repository → Domain
- 각 레이어는 **바로 아래 레이어만** 의존
- **Repository는 인터페이스로 추상화** → 나중에 구현체 교체 가능 (In-Memory → JPA)

### 3. 요청 처리 흐름

```
1. 사용자가 브라우저에서 http://localhost:8080/ 접속
                  ↓
2. Spring Boot가 요청을 BlogHomeController의 home() 메소드로 라우팅
                  ↓
3. Controller가 PostService.getAllPosts() 호출
                  ↓
4. Service가 PostRepository.findAll() 호출
                  ↓
5. Repository가 메모리에서 데이터 조회 (나중엔 DB 조회)
                  ↓
6. Post 객체 리스트가 Service → Controller로 반환
                  ↓
7. Controller가 Model에 데이터 추가 (model.addAttribute("posts", posts))
                  ↓
8. Thymeleaf가 blog/index.html 템플릿 렌더링
                  ↓
9. HTML이 사용자 브라우저로 전송됨
```

---

## 🎓 레이어별 학습 포인트

### 1️⃣ Domain Layer (도메인 레이어)

**파일**: `Post.java`

#### 학습 포인트
- ✅ **엔티티(Entity)란?** 비즈니스 개념을 표현하는 객체
- ✅ **불변성(Immutability)** 고려: final 필드 사용
- ✅ **생성자 vs Builder 패턴**: 필드가 많을 때는 Builder가 유리
- ✅ **날짜 처리**: `LocalDate` 사용 (Java 8+)

#### 코드 분석

```java
public class Post {
    private String slug;              // URL에 사용되는 고유 식별자 (예: "spring-docker-setup")
    private String title;             // 포스트 제목
    private String description;       // 짧은 설명 (미리보기)
    private String content;           // 본문 (HTML 가능)
    private String category;          // 카테고리 (예: "SPRING", "DOCKER")
    private List<String> tags;        // 태그 리스트 (예: ["Spring Boot", "Docker"])
    private LocalDate date;           // 작성일
    private String thumbnailUrl;      // 썸네일 이미지 URL (선택)
    private String summary;           // 요약 (선택)
}
```

#### 실습 문제
1. `Post` 클래스에 `viewCount` (조회수) 필드 추가해보기
2. `Builder` 패턴을 사용하도록 리팩토링해보기 (Lombok의 `@Builder` 사용)

---

### 2️⃣ Repository Layer (저장소 레이어)

**파일**: `PostRepository.java`, `InMemoryPostRepository.java`

#### 학습 포인트
- ✅ **Repository 패턴**: 데이터 접근 로직을 추상화
- ✅ **인터페이스 vs 구현체**: 의존성 역전 원칙 (DIP)
- ✅ **Optional<T>**: null 대신 사용하는 안전한 방법
- ✅ **Stream API**: Java의 함수형 프로그래밍

#### 코드 분석

**인터페이스 (추상화)**
```java
public interface PostRepository {
    List<Post> findAll();                                              // 모든 포스트 조회
    Optional<Post> findBySlug(String slug);                           // slug로 단일 포스트 조회
    List<Post> findByCategory(String category);                       // 카테고리로 필터링
    List<Post> findByTag(String tag);                                 // 태그로 필터링
    List<Post> findByCategoryExcludingSlug(String category,
                                           String excludeSlug,
                                           int limit);                // 관련 포스트 조회
}
```

**구현체 (In-Memory)**
```java
@Repository
public class InMemoryPostRepository implements PostRepository {
    private final Map<String, Post> posts = new LinkedHashMap<>();

    public InMemoryPostRepository() {
        initDummyData();  // 생성자에서 더미 데이터 초기화
    }

    @Override
    public List<Post> findAll() {
        return posts.values().stream()
            .sorted((p1, p2) -> p2.getDate().compareTo(p1.getDate()))  // 날짜 내림차순 정렬
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Post> findBySlug(String slug) {
        return Optional.ofNullable(posts.get(slug));  // null이면 Optional.empty() 반환
    }
}
```

#### 왜 인터페이스로 추상화했나?

나중에 PostgreSQL로 전환할 때 이렇게 하면 됩니다:

```java
@Repository
public class JpaPostRepository implements PostRepository {
    @Autowired
    private JpaPostRepository jpaRepo;

    @Override
    public List<Post> findAll() {
        return jpaRepo.findAllByOrderByDateDesc();  // JPA 메소드
    }

    // ... 나머지 메소드 구현
}
```

**Service와 Controller는 수정할 필요 없음!** (인터페이스에 의존하고 있으므로)

#### 실습 문제
1. `findByTitleContaining(String keyword)` 메소드 추가해보기 (제목 검색)
2. `findTop5ByOrderByDateDesc()` 메소드 추가해보기 (최신 5개)

---

### 3️⃣ Service Layer (서비스 레이어)

**파일**: `PostService.java`

#### 학습 포인트
- ✅ **비즈니스 로직의 위치**: Controller가 아닌 Service에!
- ✅ **생성자 주입(Constructor Injection)**: `@Autowired` 없이 가능
- ✅ **단일 책임 원칙(SRP)**: 각 메소드는 하나의 작업만

#### 코드 분석

```java
@Service
public class PostService {
    private final PostRepository postRepository;

    // 생성자 주입 (Spring 4.3+ 부터는 @Autowired 생략 가능)
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // 모든 포스트 조회
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // slug로 포스트 조회
    public Optional<Post> getPostBySlug(String slug) {
        return postRepository.findBySlug(slug);
    }

    // 관련 포스트 추천 (같은 카테고리, 현재 포스트 제외)
    public List<Post> getRelatedPosts(String category, String currentSlug, int limit) {
        return postRepository.findByCategoryExcludingSlug(category, currentSlug, limit);
    }
}
```

#### 왜 Service가 필요한가?

**나쁜 예 (Service 없이 Controller에서 직접)**
```java
@GetMapping("/")
public String home(Model model) {
    List<Post> posts = postRepository.findAll();

    // 비즈니스 로직이 Controller에 들어가면 재사용 불가능!
    posts = posts.stream()
        .filter(p -> p.getDate().isAfter(LocalDate.now().minusMonths(6)))
        .collect(Collectors.toList());

    model.addAttribute("posts", posts);
    return "blog/index";
}
```

**좋은 예 (Service에서 비즈니스 로직 처리)**
```java
// Service
public List<Post> getRecentPosts(int months) {
    return postRepository.findAll().stream()
        .filter(p -> p.getDate().isAfter(LocalDate.now().minusMonths(months)))
        .collect(Collectors.toList());
}

// Controller
@GetMapping("/")
public String home(Model model) {
    List<Post> posts = postService.getRecentPosts(6);  // 재사용 가능!
    model.addAttribute("posts", posts);
    return "blog/index";
}
```

#### 실습 문제
1. `getPostsByDateRange(LocalDate start, LocalDate end)` 메소드 추가
2. `getMostRecentPost()` 메소드 추가 (가장 최신 포스트 1개)

---

### 4️⃣ Controller Layer (컨트롤러 레이어)

**파일**: `BlogHomeController.java`, `PostController.java`, `CategoryController.java`

#### 학습 포인트
- ✅ **RESTful URL 설계**: `/posts/{slug}` 같은 깔끔한 URL
- ✅ **HTTP 메소드**: GET, POST, PUT, DELETE (여기서는 GET만 사용)
- ✅ **Model 객체**: View에 데이터 전달하는 방법
- ✅ **@PathVariable**: URL 경로에서 변수 추출

#### 코드 분석

**1. 블로그 홈 컨트롤러**
```java
@Controller
public class BlogHomeController {
    private final PostService postService;

    public BlogHomeController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Post> posts = postService.getAllPosts();

        // Model에 데이터 추가 (View에서 ${posts}로 접근 가능)
        model.addAttribute("posts", posts);
        model.addAttribute("totalCount", posts.size());
        model.addAttribute("pageTitle", "HOT GAMJA LAB - 기술 블로그");

        return "blog/index";  // templates/blog/index.html 렌더링
    }
}
```

**2. 포스트 상세 컨트롤러**
```java
@Controller
@RequestMapping("/posts")  // /posts로 시작하는 모든 요청
public class PostController {

    @GetMapping("/{slug}")  // /posts/spring-docker-setup
    public String postDetail(@PathVariable String slug, Model model) {
        // slug로 포스트 조회
        Optional<Post> postOptional = postService.getPostBySlug(slug);

        // 포스트가 없으면 404 페이지
        if (postOptional.isEmpty()) {
            return "error/404";
        }

        Post post = postOptional.get();

        // 관련 포스트 추천 (같은 카테고리, 최대 3개)
        List<Post> relatedPosts = postService.getRelatedPosts(
            post.getCategory(),
            post.getSlug(),
            3
        );

        model.addAttribute("post", post);
        model.addAttribute("relatedPosts", relatedPosts);
        model.addAttribute("pageTitle", post.getTitle() + " - HOT GAMJA LAB");

        return "blog/post-detail";
    }
}
```

**3. 카테고리/태그 필터 컨트롤러**
```java
@Controller
public class CategoryController {

    @GetMapping("/categories/{category}")
    public String postsByCategory(@PathVariable String category, Model model) {
        List<Post> posts = postService.getPostsByCategory(category);

        model.addAttribute("posts", posts);
        model.addAttribute("totalCount", posts.size());
        model.addAttribute("filterType", "category");
        model.addAttribute("filterValue", category);
        model.addAttribute("pageTitle", category + " 카테고리 - HOT GAMJA LAB");

        return "blog/index";  // 같은 템플릿 재사용!
    }

    @GetMapping("/tags/{tag}")
    public String postsByTag(@PathVariable String tag, Model model) {
        List<Post> posts = postService.getPostsByTag(tag);
        // ... (카테고리와 유사)
    }
}
```

#### URL 설계 원칙

| URL | 의미 | HTTP 메소드 |
|-----|------|-------------|
| `/` | 블로그 홈 | GET |
| `/posts/spring-docker-setup` | 특정 포스트 상세 | GET |
| `/categories/SPRING` | SPRING 카테고리 포스트 목록 | GET |
| `/tags/Docker` | Docker 태그 포스트 목록 | GET |

#### 실습 문제
1. `/search?q={keyword}` 엔드포인트 추가 (제목 검색)
2. `/posts/{slug}/edit` 엔드포인트 추가 (수정 폼 - 일단 GET만)

---

### 5️⃣ View Layer (뷰 레이어)

**파일**: Thymeleaf 템플릿들 (`.html`)

#### 학습 포인트
- ✅ **템플릿 상속**: `layout.html`을 베이스로 재사용
- ✅ **Fragment**: 재사용 가능한 HTML 조각 (`about.html`, `reader-controls.html`)
- ✅ **Thymeleaf 문법**: `th:text`, `th:each`, `th:if`, `th:href`
- ✅ **서버 사이드 렌더링(SSR)**: 서버에서 HTML 완성 후 전송

#### 코드 분석

**1. 레이아웃 (layout.html)**
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title th:text="${pageTitle}">HOT GAMJA LAB</title>
    <!-- CSS 로드 -->
    <link rel="stylesheet" th:href="@{/css/blog-base.css}">
    <link rel="stylesheet" th:href="@{/css/blog-layout.css}">
</head>
<body>
    <!-- About 패널 (Fragment 삽입) -->
    <div th:replace="~{blog-fragments/about :: about-panel}"></div>

    <!-- Brand 배경 (Fragment 삽입) -->
    <div th:replace="~{blog-fragments/brand-background :: brand-bg}"></div>

    <!-- 메인 콘텐츠 (자식 템플릿에서 정의) -->
    <main class="main-content" th:fragment="content">
        <!-- 여기에 자식 템플릿 내용이 삽입됨 -->
    </main>

    <!-- Reader Controls (Fragment 삽입) -->
    <div th:replace="~{blog-fragments/reader-controls :: controls}"></div>

    <!-- JavaScript 로드 -->
    <script th:src="@{/js/reader-controls.js}"></script>
    <script th:src="@{/js/scroll-horizontal.js}"></script>
</body>
</html>
```

**2. 포스트 리스트 (index.html)**
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
      th:replace="~{blog/layout :: layout(~{::main})}">
<body>
    <main class="main-content">
        <div class="content-wrapper">
            <!-- 페이지 헤더 -->
            <header class="page-header">
                <h2 class="page-title" th:if="${filterType == null}">
                    모든 글 <span class="count" th:text="|${totalCount}개|">5개</span>
                </h2>
                <h2 class="page-title" th:if="${filterType == 'category'}">
                    <span th:text="${filterValue}">SPRING</span> 카테고리
                    <span class="count" th:text="|${totalCount}개|">2개</span>
                </h2>
            </header>

            <!-- 포스트 리스트 (가로 스크롤) -->
            <div class="post-list" data-view="default">
                <!-- 각 포스트 카드 반복 -->
                <article th:each="post : ${posts}" class="post-card">
                    <a th:href="@{/posts/{slug}(slug=${post.slug})}" class="post-card-link">
                        <!-- 카테고리 배지 -->
                        <div class="post-category">
                            <span class="category-badge" th:text="${post.category}">SPRING</span>
                        </div>

                        <!-- 제목 -->
                        <h3 class="post-title" th:text="${post.title}">제목</h3>

                        <!-- 설명 -->
                        <p class="post-description" th:text="${post.description}">설명</p>

                        <!-- 메타 정보 -->
                        <div class="post-meta">
                            <time class="post-date"
                                  th:datetime="${post.date}"
                                  th:text="${#temporals.format(post.date, 'yyyy.MM.dd')}">
                                2025.01.15
                            </time>

                            <!-- 태그 리스트 -->
                            <div class="post-tags">
                                <span th:each="tag : ${post.tags}"
                                      class="tag"
                                      th:text="|#${tag}|">
                                    #Spring
                                </span>
                            </div>
                        </div>
                    </a>
                </article>
            </div>

            <!-- 빈 상태 (포스트가 없을 때) -->
            <div th:if="${#lists.isEmpty(posts)}" class="empty-state">
                <p class="empty-message">포스트가 없습니다.</p>
            </div>
        </div>
    </main>
</body>
</html>
```

#### Thymeleaf 문법 정리

| 문법 | 설명 | 예시 |
|------|------|------|
| `th:text` | 텍스트 출력 (HTML 이스케이프) | `<p th:text="${post.title}">제목</p>` |
| `th:utext` | HTML 출력 (이스케이프 안 함) | `<div th:utext="${post.content}">내용</div>` |
| `th:each` | 리스트 반복 | `<div th:each="post : ${posts}">...</div>` |
| `th:if` | 조건부 렌더링 | `<div th:if="${totalCount > 0}">...</div>` |
| `th:href` | 링크 생성 | `<a th:href="@{/posts/{slug}(slug=${post.slug})}">` |
| `th:replace` | Fragment 삽입 | `<div th:replace="~{blog-fragments/about :: about-panel}">` |
| `${}` | 변수 표현식 | `${post.title}` |
| `@{}` | URL 표현식 | `@{/css/blog-base.css}` |
| `#temporals` | 날짜 유틸리티 | `${#temporals.format(date, 'yyyy.MM.dd')}` |

#### 실습 문제
1. 포스트 조회수를 표시하는 HTML 추가해보기
2. "읽는 시간" (예: "5분") 계산해서 표시해보기 (content 길이 / 200 words per minute)

---

### 6️⃣ CSS (스타일링)

**파일**: `blog-base.css`, `blog-layout.css`, `blog-post.css`, `blog-responsive.css`

#### 학습 포인트
- ✅ **CSS 변수**: 테마 전환 쉽게 만들기
- ✅ **Flexbox & Grid**: 현대적인 레이아웃
- ✅ **반응형 디자인**: 미디어 쿼리로 모바일 최적화
- ✅ **Data Attributes**: `[data-theme="dark"]` 같은 선택자

#### 코드 분석

**1. CSS 변수로 테마 관리**
```css
/* 다크 테마 (기본값) */
:root, [data-theme="dark"] {
    --bg-primary: #0a0a0a;          /* 배경색 */
    --text-primary: #e0e0e0;        /* 텍스트 색 */
    --accent-primary: #ffffff;      /* 강조색 */
}

/* 라이트 테마 */
[data-theme="light"] {
    --bg-primary: #ffffff;
    --text-primary: #1a1a1a;
    --accent-primary: #000000;
}

/* 사용 */
body {
    background-color: var(--bg-primary);  /* 테마에 따라 자동 변경! */
    color: var(--text-primary);
}
```

**2. 폰트 크기 변수**
```css
:root {
    --font-base: 1rem;
}

/* Small 폰트 */
[data-font-size="S"] {
    --font-base: 0.875rem;    /* 14px */
}

/* Large 폰트 */
[data-font-size="L"] {
    --font-base: 1.125rem;    /* 18px */
}

/* 사용 */
.post-description {
    font-size: var(--font-base);  /* 폰트 크기 설정에 따라 자동 변경! */
}
```

**3. Flexbox로 가로 스크롤 리스트**
```css
.post-list {
    display: flex;              /* 가로 배치 */
    gap: 2rem;                  /* 카드 간격 */
    overflow-x: auto;           /* 가로 스크롤 */
    overflow-y: hidden;
}

.post-card {
    flex: 0 0 400px;           /* 고정 너비 400px */
    width: 400px;
}
```

**4. Grid로 이미지 타일 모드**
```css
.post-list[data-view="image"] {
    display: grid;                                          /* 그리드 레이아웃 */
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));  /* 자동으로 열 생성 */
    gap: 2rem;
    overflow-x: visible;       /* 가로 스크롤 해제 */
}
```

**5. 반응형 디자인**
```css
/* 모바일 (~ 768px) */
@media (max-width: 768px) {
    .about-panel {
        position: relative;    /* 고정 해제 */
        width: 100%;           /* 전체 너비 */
        height: auto;
    }

    .main-content {
        margin-left: 0;        /* 왼쪽 여백 제거 */
    }

    .post-list {
        flex-direction: column;  /* 세로 배치 */
    }

    .post-card {
        width: 100%;           /* 전체 너비 */
    }
}
```

#### 실습 문제
1. 새로운 테마 추가해보기 (예: `[data-theme="blue"]`)
2. XL 폰트 크기 추가해보기 (`[data-font-size="XL"]`)

---

### 7️⃣ JavaScript (인터랙션)

**파일**: `reader-controls.js`, `scroll-horizontal.js`

#### 학습 포인트
- ✅ **DOM 조작**: `document.querySelector`, `addEventListener`
- ✅ **localStorage**: 브라우저에 데이터 저장
- ✅ **이벤트 처리**: wheel, click, mousedown/move/up
- ✅ **MutationObserver**: DOM 변경 감지

#### 코드 분석

**1. Reader Controls (reader-controls.js)**

```javascript
// 페이지 로드 시 저장된 설정 불러오기
function loadSettings() {
    const savedFontSize = localStorage.getItem('blog-font-size') || 'M';
    const savedTheme = localStorage.getItem('blog-theme') || 'dark';
    const savedViewMode = localStorage.getItem('blog-view-mode') || 'default';

    applyFontSize(savedFontSize);
    applyTheme(savedTheme);
    applyViewMode(savedViewMode);
}

// 폰트 크기 적용
function applyFontSize(size) {
    // <html> 태그에 data-font-size 속성 변경
    document.documentElement.setAttribute('data-font-size', size);

    // 버튼 active 상태 업데이트
    fontSizeButtons.forEach(button => {
        const buttonSize = button.getAttribute('data-font-size');
        if (buttonSize === size) {
            button.classList.add('active');
        } else {
            button.classList.remove('active');
        }
    });
}

// 버튼 클릭 이벤트
fontSizeButtons.forEach(button => {
    button.addEventListener('click', () => {
        const size = button.getAttribute('data-font-size');
        applyFontSize(size);
        localStorage.setItem('blog-font-size', size);  // 저장!
    });
});
```

**2. 가로 스크롤 (scroll-horizontal.js)**

```javascript
// 마우스 휠로 가로 스크롤
postList.addEventListener('wheel', (e) => {
    // 세로 스크롤이 아니면 무시
    if (e.deltaY === 0) return;

    // 이미지 모드면 무시
    const viewMode = postList.getAttribute('data-view');
    if (viewMode === 'image') return;

    // 기본 스크롤 동작 막기
    e.preventDefault();

    // 가로 스크롤 실행
    const scrollAmount = e.deltaY * SCROLL_SPEED;
    postList.scrollLeft += scrollAmount;
}, { passive: false });  // preventDefault 허용

// 드래그 스크롤
let isDragging = false;
let startX = 0;
let scrollLeft = 0;

postList.addEventListener('mousedown', (e) => {
    isDragging = true;
    startX = e.pageX - postList.offsetLeft;
    scrollLeft = postList.scrollLeft;
    postList.style.cursor = 'grabbing';
});

postList.addEventListener('mousemove', (e) => {
    if (!isDragging) return;
    e.preventDefault();

    const x = e.pageX - postList.offsetLeft;
    const walk = (x - startX) * 2;  // 이동 거리 증폭
    postList.scrollLeft = scrollLeft - walk;
});

postList.addEventListener('mouseup', () => {
    isDragging = false;
    postList.style.cursor = 'grab';
});
```

**3. 모바일 감지**
```javascript
function isMobileDevice() {
    // 1. User Agent로 확인
    const userAgent = navigator.userAgent.toLowerCase();
    const mobileKeywords = ['mobile', 'android', 'iphone', 'ipad'];
    const isMobileUA = mobileKeywords.some(keyword => userAgent.includes(keyword));

    // 2. 터치 지원 여부 확인
    const hasTouch = 'ontouchstart' in window || navigator.maxTouchPoints > 0;

    // 3. 화면 너비 확인
    const isSmallScreen = window.innerWidth <= 768;

    return isMobileUA || (hasTouch && isSmallScreen);
}
```

#### 실습 문제
1. 키보드 화살표로 가로 스크롤 구현해보기 (좌/우 화살표)
2. 스크롤 진행률 표시 추가해보기 (예: "20%" 같은 표시)

---

## 🔍 주요 기능 코드 분석

### 기능 1: 카테고리/태그 필터링

**흐름**
1. 사용자가 카테고리 배지 클릭 → `/categories/SPRING`
2. `CategoryController.postsByCategory()` 실행
3. `PostService.getPostsByCategory("SPRING")` 호출
4. `PostRepository.findByCategory("SPRING")` 실행
5. SPRING 카테고리 포스트만 필터링해서 반환
6. `blog/index.html` 템플릿 렌더링 (필터 정보 표시)

**코드**
```java
// Controller
@GetMapping("/categories/{category}")
public String postsByCategory(@PathVariable String category, Model model) {
    List<Post> posts = postService.getPostsByCategory(category);
    model.addAttribute("posts", posts);
    model.addAttribute("filterType", "category");  // 템플릿에서 필터 표시
    model.addAttribute("filterValue", category);
    return "blog/index";
}

// Service
public List<Post> getPostsByCategory(String category) {
    return postRepository.findByCategory(category);
}

// Repository (In-Memory)
@Override
public List<Post> findByCategory(String category) {
    return posts.values().stream()
        .filter(post -> post.getCategory().equalsIgnoreCase(category))
        .sorted((p1, p2) -> p2.getDate().compareTo(p1.getDate()))
        .collect(Collectors.toList());
}
```

**템플릿**
```html
<!-- 필터 정보 표시 -->
<h2 class="page-title" th:if="${filterType == 'category'}">
    <span th:text="${filterValue}">SPRING</span> 카테고리
    <span class="count" th:text="|${totalCount}개|">2개</span>
</h2>
```

---

### 기능 2: 관련 포스트 추천

**흐름**
1. 사용자가 포스트 상세 페이지 접속 → `/posts/spring-docker-setup`
2. 현재 포스트의 카테고리 확인 (예: "SPRING")
3. 같은 카테고리의 다른 포스트 조회 (현재 포스트 제외, 최대 3개)
4. 날짜 내림차순 정렬
5. 페이지 하단에 "관련 포스트" 섹션 표시

**코드**
```java
// Controller
@GetMapping("/{slug}")
public String postDetail(@PathVariable String slug, Model model) {
    Optional<Post> postOptional = postService.getPostBySlug(slug);
    if (postOptional.isEmpty()) {
        return "error/404";
    }

    Post post = postOptional.get();

    // 관련 포스트 추천
    List<Post> relatedPosts = postService.getRelatedPosts(
        post.getCategory(),  // 같은 카테고리
        post.getSlug(),      // 현재 포스트 제외
        3                    // 최대 3개
    );

    model.addAttribute("post", post);
    model.addAttribute("relatedPosts", relatedPosts);
    return "blog/post-detail";
}

// Repository
@Override
public List<Post> findByCategoryExcludingSlug(String category, String excludeSlug, int limit) {
    return posts.values().stream()
        .filter(post -> post.getCategory().equalsIgnoreCase(category))
        .filter(post -> !post.getSlug().equals(excludeSlug))  // 현재 포스트 제외
        .sorted((p1, p2) -> p2.getDate().compareTo(p1.getDate()))
        .limit(limit)
        .collect(Collectors.toList());
}
```

---

### 기능 3: 테마 전환

**흐름**
1. 사용자가 "Light" 버튼 클릭
2. JavaScript가 `<html>` 태그의 `data-theme` 속성을 "light"로 변경
3. CSS 변수가 자동으로 라이트 테마 값으로 변경
4. localStorage에 설정 저장
5. 페이지 새로고침 시 저장된 테마 자동 적용

**코드**
```javascript
// JavaScript
function applyTheme(theme) {
    document.documentElement.setAttribute('data-theme', theme);

    // 버튼 active 상태 업데이트
    themeButtons.forEach(button => {
        const buttonTheme = button.getAttribute('data-theme');
        if (buttonTheme === theme) {
            button.classList.add('active');
        } else {
            button.classList.remove('active');
        }
    });
}

// 버튼 클릭 이벤트
themeButtons.forEach(button => {
    button.addEventListener('click', () => {
        const theme = button.getAttribute('data-theme');
        applyTheme(theme);
        localStorage.setItem('blog-theme', theme);
    });
});

// 페이지 로드 시
function loadSettings() {
    const savedTheme = localStorage.getItem('blog-theme') || 'dark';
    applyTheme(savedTheme);
}
```

```css
/* CSS */
[data-theme="dark"] {
    --bg-primary: #0a0a0a;
    --text-primary: #e0e0e0;
}

[data-theme="light"] {
    --bg-primary: #ffffff;
    --text-primary: #1a1a1a;
}

body {
    background-color: var(--bg-primary);  /* 자동 변경! */
    color: var(--text-primary);
}
```

---

## 📖 학습 순서 추천

### 초급: Backend 기초 (1-2주)

1. **Domain 이해하기** (1일)
   - `Post.java` 읽고 이해하기
   - 실습: 필드 추가해보기 (`viewCount`, `author` 등)

2. **Repository 패턴** (2-3일)
   - 인터페이스와 구현체 분리 이해하기
   - `InMemoryPostRepository` 코드 분석
   - 실습: 새로운 조회 메소드 추가

3. **Service 레이어** (2일)
   - 비즈니스 로직의 역할 이해하기
   - 실습: 검색 기능 추가

4. **Controller 레이어** (3일)
   - HTTP 요청 처리 이해하기
   - Model 객체 사용법
   - 실습: 새로운 엔드포인트 추가

### 중급: Frontend 기초 (1-2주)

5. **Thymeleaf 기본** (3일)
   - `th:text`, `th:each`, `th:if` 문법
   - Fragment 사용법
   - 실습: 포스트 수정 폼 만들기

6. **CSS 레이아웃** (4일)
   - Flexbox vs Grid
   - CSS 변수 활용
   - 반응형 디자인
   - 실습: 새로운 레이아웃 섹션 추가

7. **JavaScript 기초** (5일)
   - DOM 조작
   - 이벤트 처리
   - localStorage 활용
   - 실습: 포스트 북마크 기능 추가

### 고급: 실전 응용 (2-4주)

8. **데이터베이스 연동** (1주)
   - PostgreSQL 설정
   - JPA Repository 구현
   - 마이그레이션 스크립트

9. **CRUD 기능 추가** (1주)
   - 포스트 작성/수정/삭제
   - 폼 유효성 검사
   - 이미지 업로드

10. **추가 기능** (1-2주)
    - 검색 엔진
    - 페이지네이션
    - 댓글 시스템
    - RSS 피드

---

## 🛠 실습 방법

### 1. 프로젝트 실행하기

```bash
# 1. 프로젝트 디렉토리로 이동
cd /home/hot-gamja/WorkSpace/spring-boot-thymeleaf

# 2. 빌드
./gradlew clean build -x test

# 3. 실행
./gradlew bootRun

# 4. 브라우저에서 확인
# http://localhost:8080
```

### 2. 코드 수정 후 확인하기

**Spring Boot DevTools가 설치되어 있어서 자동 재시작됩니다!**

- **Java 파일** 수정 후: 자동으로 재시작 (5-10초 소요)
- **HTML/CSS/JS 파일** 수정 후: 브라우저 새로고침만 하면 됨

### 3. 디버깅 방법

#### Backend 디버깅
```java
// Controller에 로그 추가
@GetMapping("/")
public String home(Model model) {
    List<Post> posts = postService.getAllPosts();
    System.out.println("📚 포스트 개수: " + posts.size());  // 콘솔에 출력
    model.addAttribute("posts", posts);
    return "blog/index";
}
```

#### Frontend 디버깅
```javascript
// JavaScript에서
console.log('📏 폰트 크기 변경:', size);
console.log('🎨 테마 변경:', theme);

// 브라우저 개발자 도구 (F12) → Console 탭에서 확인
```

### 4. 실습 과제 예시

#### 과제 1: 조회수 기능 추가

**난이도**: ⭐ (쉬움)

1. `Post.java`에 `viewCount` 필드 추가
2. `PostService`에 `incrementViewCount(String slug)` 메소드 추가
3. `PostController`에서 포스트 조회 시 조회수 증가 호출
4. 템플릿에 조회수 표시

#### 과제 2: 검색 기능 추가

**난이도**: ⭐⭐ (중간)

1. `PostRepository`에 `findByTitleContaining(String keyword)` 메소드 추가
2. `PostService`에 검색 로직 추가
3. `SearchController` 생성 (`GET /search?q={keyword}`)
4. 검색 폼 추가 (About 패널에)
5. 검색 결과 페이지 표시

#### 과제 3: 페이지네이션 추가

**난이도**: ⭐⭐⭐ (어려움)

1. `PostRepository`에 `findAllPaged(int page, int size)` 메소드 추가
2. `PostService`에 페이징 로직 추가
3. `BlogHomeController`에 `@RequestParam int page` 추가
4. 템플릿에 페이지네이션 UI 추가
5. CSS로 페이지네이션 버튼 스타일링

---

## 🔧 문제 해결 가이드

### 문제 1: 포트 8080이 이미 사용 중

```bash
# 에러 메시지
Web server failed to start. Port 8080 was already in use.

# 해결 방법 1: 포트 변경
# application.properties 파일에 추가
server.port=8081

# 해결 방법 2: 기존 프로세스 종료
lsof -i :8080
kill -9 [PID]
```

### 문제 2: Thymeleaf 템플릿을 찾을 수 없음

```
org.thymeleaf.exceptions.TemplateInputException: Error resolving template "blog/index"
```

**원인**: 템플릿 파일 경로가 잘못됨

**확인사항**:
- `src/main/resources/templates/blog/index.html` 존재하는지 확인
- 파일 이름 철자 확인 (대소문자 구분!)
- Controller에서 `return "blog/index";` 확인

### 문제 3: CSS/JS가 로드되지 않음

**원인**: 정적 리소스 경로 오류

**확인사항**:
- `src/main/resources/static/css/blog-base.css` 존재하는지 확인
- Thymeleaf에서 `th:href="@{/css/blog-base.css}"` 사용했는지 확인 (`/`로 시작!)
- 브라우저 개발자 도구 (F12) → Network 탭에서 404 에러 확인

### 문제 4: 가로 스크롤이 작동하지 않음

**확인사항**:
1. 모바일 기기인가? → 콘솔에 "📱 모바일 기기: 가로 스크롤 비활성화" 로그 확인
2. View 모드가 "image"인가? → 이미지 모드에서는 가로 스크롤 비활성화됨
3. `.post-list` 요소가 있는가? → `document.querySelector('.post-list')` 확인

### 문제 5: localStorage 설정이 저장되지 않음

**원인**: 브라우저 프라이빗 모드 또는 쿠키 차단

**해결 방법**:
```javascript
// 콘솔에서 확인
localStorage.getItem('blog-font-size')  // null이면 저장 안 된 것

// 수동으로 설정해보기
localStorage.setItem('blog-font-size', 'L')

// 브라우저 설정 → 쿠키 허용 확인
```

---

## 🎉 마무리

### 당신이 배운 것

- ✅ **Spring Boot 레이어드 아키텍처**: Controller → Service → Repository → Domain
- ✅ **Thymeleaf 템플릿 엔진**: 서버 사이드 렌더링의 기초
- ✅ **Repository 패턴**: 데이터 접근 로직 추상화
- ✅ **의존성 주입(DI)**: Spring의 핵심 개념
- ✅ **RESTful URL 설계**: 깔끔한 URL 구조
- ✅ **CSS 변수**: 테마 전환을 쉽게 만드는 방법
- ✅ **Flexbox & Grid**: 현대적인 레이아웃
- ✅ **반응형 디자인**: 모바일/태블릿/PC 대응
- ✅ **Vanilla JavaScript**: 프레임워크 없이 인터랙션 구현
- ✅ **localStorage**: 브라우저 저장소 활용

### 다음 단계

`NEXT_STEPS.md` 파일을 참고하세요!

1. PostgreSQL 연동
2. CRUD 기능 (Create, Read, Update, Delete)
3. 검색 엔진
4. 페이지네이션
5. 댓글 시스템
6. 배포 (Docker, AWS)

### 추천 학습 자료

- **Spring Boot 공식 문서**: https://spring.io/guides
- **Thymeleaf 문서**: https://www.thymeleaf.org/documentation.html
- **Java Stream API**: https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html
- **MDN Web Docs (HTML/CSS/JS)**: https://developer.mozilla.org

---

**만든 사람**: HOT GAMJA
**문의**: 코드에 주석을 달아놨으니 천천히 읽어보세요!
**라이선스**: 마음껏 배우고 수정하세요 🚀
