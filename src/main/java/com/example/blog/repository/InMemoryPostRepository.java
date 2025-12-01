package com.example.blog.repository;

import com.example.blog.domain.Post;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * In-Memory 방식으로 포스트 데이터를 관리하는 Repository 구현체
 *
 * 특징:
 * - 실제 DB 없이 Map으로 데이터 저장
 * - 애플리케이션 시작 시 더미 데이터 자동 생성
 * - 나중에 PostgreSQL + JPA로 전환 시 이 클래스만 교체하면 됨
 *
 * @Repository: 이 클래스가 Spring의 빈으로 등록되어 다른 곳에서 주입받을 수 있게 함
 */
@Repository
public class InMemoryPostRepository implements PostRepository {

    /**
     * 포스트 저장소 (slug를 key로 사용)
     *
     * 예:
     * {
     *   "spring-docker-setup" -> Post 객체,
     *   "postgresql-docker" -> Post 객체,
     *   ...
     * }
     */
    private final Map<String, Post> posts = new LinkedHashMap<>();

    /**
     * 생성자: 애플리케이션 시작 시 더미 데이터를 자동으로 생성
     */
    public InMemoryPostRepository() {
        initDummyData();
    }

    /**
     * 더미 데이터 초기화
     *
     * 여러 개의 예시 포스트를 생성해서 Map에 저장합니다.
     * 실제 프로젝트에서는 이 데이터를 DB에서 불러오겠지만,
     * 지금은 학습/테스트 목적으로 하드코딩합니다.
     */
    private void initDummyData() {
        // 포스트 1: Spring Boot + Docker 환경 구축
        Post post1 = new Post(
                "spring-docker-setup",
                "Spring Boot와 Docker로 로컬 개발 환경 구축하기",
                "Docker Compose를 활용해 Spring Boot 애플리케이션을 컨테이너로 실행하는 방법을 알아봅니다.",
                """
                <h2>들어가며</h2>
                <p>
                    로컬 개발 환경을 Docker로 구축하면 팀원들과 동일한 환경을 공유할 수 있습니다.
                    이번 글에서는 Spring Boot 앱을 Docker Compose로 실행하는 방법을 단계별로 정리합니다.
                </p>

                <h2>1. Dockerfile 작성</h2>
                <p>먼저 Spring Boot 애플리케이션을 컨테이너화하기 위한 Dockerfile을 작성합니다.</p>
                <pre><code>FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]</code></pre>

                <h2>2. Docker Compose 설정</h2>
                <p>docker-compose.yml 파일로 애플리케이션과 DB를 함께 실행합니다.</p>
                <pre><code>version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - db
  db:
    image: postgres:15
    environment:
      POSTGRES_DB: mydb
      POSTGRES_USER: user
      POSTGRES_PASSWORD: password</code></pre>

                <h2>3. 실행</h2>
                <p>다음 명령어로 전체 환경을 한 번에 실행할 수 있습니다.</p>
                <pre><code>docker-compose up -d</code></pre>

                <h2>마무리</h2>
                <p>
                    이제 팀원 누구나 동일한 환경에서 개발할 수 있습니다.
                    다음 글에서는 PostgreSQL 설정을 더 자세히 다루겠습니다.
                </p>
                """,
                "SPRING",
                Arrays.asList("Spring Boot", "Docker", "Docker Compose"),
                LocalDate.of(2025, 1, 15),
                null,
                "Docker Compose를 사용하면 로컬 개발 환경을 쉽게 구축하고 팀원들과 공유할 수 있습니다."
        );

        // 포스트 2: PostgreSQL Docker 세팅
        Post post2 = new Post(
                "postgresql-docker",
                "PostgreSQL을 Docker로 띄우고 Spring Boot와 연동하기",
                "Docker로 PostgreSQL을 실행하고 Spring Data JPA와 연결하는 방법을 정리합니다.",
                """
                <h2>왜 PostgreSQL을 Docker로?</h2>
                <p>
                    로컬에 PostgreSQL을 직접 설치하면 버전 관리가 어렵고,
                    팀원마다 다른 설정을 사용할 수 있습니다.
                    Docker를 사용하면 이런 문제를 해결할 수 있습니다.
                </p>

                <h2>1. PostgreSQL 컨테이너 실행</h2>
                <pre><code>docker run -d \\
  --name postgres-dev \\
  -e POSTGRES_USER=myuser \\
  -e POSTGRES_PASSWORD=mypassword \\
  -e POSTGRES_DB=techblog \\
  -p 5432:5432 \\
  postgres:15</code></pre>

                <h2>2. Spring Boot 설정</h2>
                <p>application.yml에 DB 연결 정보를 추가합니다.</p>
                <pre><code>spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/techblog
    username: myuser
    password: mypassword
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true</code></pre>

                <h2>3. 연결 테스트</h2>
                <p>간단한 엔티티를 만들어서 DB 연결을 테스트합니다.</p>
                <pre><code>@Entity
public class TestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}</code></pre>

                <h2>정리</h2>
                <p>
                    이제 PostgreSQL이 Docker 컨테이너로 실행되고,
                    Spring Boot에서 정상적으로 연결됩니다.
                </p>
                """,
                "DATABASE",
                Arrays.asList("PostgreSQL", "Docker", "Spring Data JPA"),
                LocalDate.of(2025, 1, 20),
                null,
                "Docker로 PostgreSQL을 실행하면 로컬 환경을 깔끔하게 유지하고 팀원들과 동일한 DB 버전을 사용할 수 있습니다."
        );

        // 포스트 3: 알고리즘 문제 풀이 회고
        Post post3 = new Post(
                "algorithm-binary-search",
                "이진 탐색(Binary Search) 완전 정복",
                "이진 탐색의 원리와 구현, 그리고 실전 문제 풀이까지 정리합니다.",
                """
                <h2>이진 탐색이란?</h2>
                <p>
                    정렬된 배열에서 특정 값을 찾을 때,
                    중간값과 비교하면서 탐색 범위를 절반씩 줄여나가는 알고리즘입니다.
                    시간 복잡도는 O(log N)입니다.
                </p>

                <h2>기본 구현 (반복문)</h2>
                <pre><code>public int binarySearch(int[] arr, int target) {
    int left = 0;
    int right = arr.length - 1;

    while (left <= right) {
        int mid = left + (right - left) / 2;

        if (arr[mid] == target) {
            return mid;
        } else if (arr[mid] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }

    return -1; // 못 찾음
}</code></pre>

                <h2>재귀 버전</h2>
                <pre><code>public int binarySearchRecursive(int[] arr, int target, int left, int right) {
    if (left > right) return -1;

    int mid = left + (right - left) / 2;

    if (arr[mid] == target) return mid;
    if (arr[mid] < target) return binarySearchRecursive(arr, target, mid + 1, right);
    return binarySearchRecursive(arr, target, left, mid - 1);
}</code></pre>

                <h2>주의할 점</h2>
                <p>
                    1. 배열이 정렬되어 있어야 합니다.<br>
                    2. mid 계산 시 오버플로우 방지: (left + right) / 2 대신 left + (right - left) / 2<br>
                    3. left <= right 조건 주의 (등호 포함!)
                </p>

                <h2>실전 활용</h2>
                <p>
                    이진 탐색은 단순히 값을 찾는 것뿐만 아니라,
                    "조건을 만족하는 최소/최대 값"을 찾는 문제에도 응용됩니다.
                    예: 백준 1654번 랜선 자르기
                </p>
                """,
                "ALGORITHM",
                Arrays.asList("Java", "Algorithm", "Binary Search"),
                LocalDate.of(2025, 1, 25),
                null,
                "이진 탐색은 정렬된 데이터에서 효율적으로 값을 찾는 필수 알고리즘입니다."
        );

        // 포스트 4: Thymeleaf 템플릿 엔진 소개
        Post post4 = new Post(
                "thymeleaf-basics",
                "Thymeleaf로 서버 사이드 렌더링 시작하기",
                "Spring Boot와 Thymeleaf를 사용해 동적인 HTML을 생성하는 방법을 알아봅니다.",
                """
                <h2>Thymeleaf란?</h2>
                <p>
                    Thymeleaf는 Spring Boot에서 공식 지원하는 템플릿 엔진입니다.
                    JSP와 달리 순수 HTML 문법을 유지하면서 동적 데이터를 삽입할 수 있습니다.
                </p>

                <h2>기본 문법</h2>
                <pre><code>&lt;!-- 변수 출력 --&gt;
&lt;p th:text="${message}"&gt;기본값&lt;/p&gt;

&lt;!-- 반복문 --&gt;
&lt;ul&gt;
    &lt;li th:each="item : ${items}" th:text="${item.name}"&gt;&lt;/li&gt;
&lt;/ul&gt;

&lt;!-- 조건문 --&gt;
&lt;div th:if="${user != null}"&gt;
    &lt;p th:text="${user.name}"&gt;&lt;/p&gt;
&lt;/div&gt;</code></pre>

                <h2>Fragment 활용</h2>
                <p>공통 레이아웃을 재사용하려면 Fragment를 사용합니다.</p>
                <pre><code>&lt;!-- layout.html --&gt;
&lt;header th:fragment="header"&gt;
    &lt;h1&gt;내 블로그&lt;/h1&gt;
&lt;/header&gt;

&lt;!-- index.html --&gt;
&lt;div th:replace="~{layout :: header}"&gt;&lt;/div&gt;</code></pre>

                <h2>Controller에서 데이터 전달</h2>
                <pre><code>@GetMapping("/")
public String home(Model model) {
    model.addAttribute("message", "안녕하세요!");
    return "index"; // templates/index.html
}</code></pre>

                <h2>정리</h2>
                <p>
                    Thymeleaf는 백엔드 개발자가 프론트 작업을 쉽게 할 수 있게 도와줍니다.
                    React/Vue 같은 SPA 프레임워크 없이도 충분히 동적인 웹을 만들 수 있습니다.
                </p>
                """,
                "SPRING",
                Arrays.asList("Spring Boot", "Thymeleaf", "Template Engine"),
                LocalDate.of(2025, 1, 28),
                null,
                "Thymeleaf는 Spring Boot에서 서버 사이드 렌더링을 구현하는 가장 쉬운 방법입니다."
        );

        // 포스트 5: Docker Compose 실전 팁
        Post post5 = new Post(
                "docker-compose-tips",
                "Docker Compose 실전 팁 모음",
                "Docker Compose를 실무에서 사용하며 배운 유용한 팁들을 공유합니다.",
                """
                <h2>1. 환경 변수 파일 분리</h2>
                <p>.env 파일로 민감한 정보를 관리합니다.</p>
                <pre><code># .env
POSTGRES_PASSWORD=secret123
DB_NAME=mydb

# docker-compose.yml
services:
  db:
    image: postgres:15
    environment:
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
      POSTGRES_DB: ${DB_NAME}</code></pre>

                <h2>2. 볼륨으로 데이터 영속화</h2>
                <pre><code>services:
  db:
    image: postgres:15
    volumes:
      - postgres-data:/var/lib/postgresql/data

volumes:
  postgres-data:</code></pre>

                <h2>3. depends_on으로 실행 순서 제어</h2>
                <pre><code>services:
  app:
    depends_on:
      - db  # db가 먼저 시작됨
  db:
    image: postgres:15</code></pre>

                <h2>4. 로그 확인</h2>
                <pre><code># 전체 서비스 로그
docker-compose logs -f

# 특정 서비스만
docker-compose logs -f app</code></pre>

                <h2>5. 개발/운영 환경 분리</h2>
                <pre><code># docker-compose.dev.yml
services:
  app:
    build:
      context: .
      dockerfile: Dockerfile.dev
    volumes:
      - .:/app  # 코드 변경 시 자동 반영

# 실행
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up</code></pre>
                """,
                "DOCKER",
                Arrays.asList("Docker", "Docker Compose", "DevOps"),
                LocalDate.of(2025, 2, 1),
                null,
                "Docker Compose를 효율적으로 사용하는 실전 팁들을 정리했습니다."
        );

        // Map에 추가 (slug를 key로 사용)
        posts.put(post1.getSlug(), post1);
        posts.put(post2.getSlug(), post2);
        posts.put(post3.getSlug(), post3);
        posts.put(post4.getSlug(), post4);
        posts.put(post5.getSlug(), post5);
    }

    @Override
    public List<Post> findAll() {
        // 날짜 최신순으로 정렬해서 반환
        return posts.values().stream()
                .sorted((p1, p2) -> p2.getDate().compareTo(p1.getDate()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Post> findBySlug(String slug) {
        return Optional.ofNullable(posts.get(slug));
    }

    @Override
    public List<Post> findByCategory(String category) {
        return posts.values().stream()
                .filter(post -> post.getCategory().equalsIgnoreCase(category))
                .sorted((p1, p2) -> p2.getDate().compareTo(p1.getDate()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> findByTag(String tag) {
        return posts.values().stream()
                .filter(post -> post.getTags() != null &&
                        post.getTags().stream()
                                .anyMatch(t -> t.equalsIgnoreCase(tag)))
                .sorted((p1, p2) -> p2.getDate().compareTo(p1.getDate()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> findByCategoryExcludingSlug(String category, String excludeSlug, int limit) {
        return posts.values().stream()
                .filter(post -> post.getCategory().equalsIgnoreCase(category))
                .filter(post -> !post.getSlug().equals(excludeSlug))
                .sorted((p1, p2) -> p2.getDate().compareTo(p1.getDate()))
                .limit(limit)
                .collect(Collectors.toList());
    }
}
