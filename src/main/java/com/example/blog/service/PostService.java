package com.example.blog.service;

import com.example.blog.domain.Post;
import com.example.blog.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 블로그 포스트 관련 비즈니스 로직을 담당하는 Service 클래스
 *
 * Service 레이어의 역할:
 * 1. Controller와 Repository 사이의 중간 계층
 * 2. 복잡한 비즈니스 로직 처리 (여러 Repository를 조합하거나, 데이터 가공 등)
 * 3. 트랜잭션 관리 (나중에 @Transactional 사용)
 *
 * 현재는 단순히 Repository 메서드를 호출하는 수준이지만,
 * 나중에 캐싱, 권한 체크, 알림 발송 같은 추가 로직이 들어갈 수 있습니다.
 *
 * @Service: 이 클래스가 Spring의 빈으로 등록되어 Controller에서 주입받을 수 있게 함
 */
@Service
public class PostService {

    /**
     * PostRepository 주입
     *
     * Spring이 자동으로 InMemoryPostRepository 구현체를 찾아서 주입합니다.
     * 나중에 JpaPostRepository로 바꿔도 이 Service 코드는 수정할 필요 없습니다.
     */
    private final PostRepository postRepository;

    /**
     * 생성자 주입 (권장 방식)
     *
     * @param postRepository PostRepository 구현체
     *
     * 생성자 주입을 사용하는 이유:
     * 1. final로 선언 가능 (불변성 보장)
     * 2. 테스트 시 Mock 객체 주입이 쉬움
     * 3. 순환 참조 문제 방지
     */
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    /**
     * 전체 포스트 목록 조회 (최신순)
     *
     * @return 전체 포스트 리스트
     *
     * 사용 예:
     * - 블로그 홈 화면에서 모든 글 보여주기
     */
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    /**
     * slug로 포스트 상세 조회
     *
     * @param slug 포스트의 고유 식별자
     * @return 해당 포스트 (없으면 Optional.empty())
     *
     * 사용 예:
     * - 포스트 상세 페이지 (/posts/spring-docker-setup)
     *
     * Controller에서는 이렇게 사용:
     * Optional<Post> post = postService.getPostBySlug(slug);
     * if (post.isEmpty()) {
     *     // 404 에러 처리
     * }
     */
    public Optional<Post> getPostBySlug(String slug) {
        return postRepository.findBySlug(slug);
    }

    /**
     * 특정 카테고리의 포스트만 조회
     *
     * @param category 카테고리 이름 (예: "SPRING", "DOCKER")
     * @return 해당 카테고리의 포스트 리스트
     *
     * 사용 예:
     * - 카테고리별 필터 페이지 (/categories/SPRING)
     */
    public List<Post> getPostsByCategory(String category) {
        return postRepository.findByCategory(category);
    }

    /**
     * 특정 태그를 포함하는 포스트 조회
     *
     * @param tag 태그 이름 (예: "Java", "Docker Compose")
     * @return 해당 태그를 포함한 포스트 리스트
     *
     * 사용 예:
     * - 태그별 필터 페이지 (/tags/Java)
     */
    public List<Post> getPostsByTag(String tag) {
        return postRepository.findByTag(tag);
    }

    /**
     * 관련 포스트 추천 (같은 카테고리, 현재 글 제외)
     *
     * @param category 현재 포스트의 카테고리
     * @param currentSlug 현재 보고 있는 포스트의 slug
     * @param limit 최대 몇 개까지 가져올지
     * @return 관련 포스트 리스트
     *
     * 사용 예:
     * - 포스트 상세 페이지 하단의 "관련 포스트" 섹션
     * - 보통 2~3개 정도 보여줌
     *
     * 예시 코드:
     * List<Post> related = postService.getRelatedPosts("SPRING", "spring-docker-setup", 3);
     */
    public List<Post> getRelatedPosts(String category, String currentSlug, int limit) {
        return postRepository.findByCategoryExcludingSlug(category, currentSlug, limit);
    }

    /**
     * 전체 포스트 개수 조회
     *
     * @return 전체 포스트 개수
     *
     * 사용 예:
     * - 홈 화면에 "총 N개의 글" 표시
     * - 페이지네이션 구현 시
     */
    public int getTotalPostCount() {
        return getAllPosts().size();
    }

    /**
     * 특정 카테고리의 포스트 개수 조회
     *
     * @param category 카테고리 이름
     * @return 해당 카테고리의 포스트 개수
     *
     * 사용 예:
     * - 카테고리 필터 옆에 개수 표시 (예: "SPRING (5)")
     */
    public int getPostCountByCategory(String category) {
        return getPostsByCategory(category).size();
    }
}
