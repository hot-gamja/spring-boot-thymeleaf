package com.example.blog.repository;

import com.example.blog.domain.Post;

import java.util.List;
import java.util.Optional;

/**
 * 포스트 데이터 접근을 담당하는 Repository 인터페이스
 *
 * 이 인터페이스를 정의하는 이유:
 * 1. 현재는 InMemoryPostRepository로 더미 데이터를 제공
 * 2. 나중에 JPA를 사용하는 JpaPostRepository로 교체 가능
 * 3. Service 레이어는 이 인터페이스에만 의존하므로, 구현체가 바뀌어도 Service 코드 수정 불필요
 *
 * 이런 패턴을 "의존성 역전 원칙(DIP)"이라고 합니다.
 */
public interface PostRepository {

    /**
     * 모든 포스트를 조회 (최신순 정렬)
     *
     * @return 전체 포스트 리스트
     */
    List<Post> findAll();

    /**
     * slug로 포스트 조회
     *
     * @param slug 포스트의 고유 식별자 (URL 경로에 사용)
     * @return 해당 slug를 가진 포스트 (없으면 Optional.empty())
     *
     * Optional을 사용하는 이유:
     * - null 반환 시 NullPointerException 위험이 있음
     * - Optional을 사용하면 "값이 없을 수 있다"는 것을 명시적으로 표현
     */
    Optional<Post> findBySlug(String slug);

    /**
     * 특정 카테고리의 포스트만 조회
     *
     * @param category 카테고리 이름 (예: "SPRING", "DOCKER")
     * @return 해당 카테고리의 포스트 리스트
     */
    List<Post> findByCategory(String category);

    /**
     * 특정 태그를 포함하는 포스트 조회
     *
     * @param tag 태그 이름 (예: "Java", "Docker Compose")
     * @return 해당 태그를 포함한 포스트 리스트
     */
    List<Post> findByTag(String tag);

    /**
     * 특정 카테고리의 포스트 중 지정한 slug를 제외한 나머지 조회
     * (관련 포스트 추천에 사용)
     *
     * @param category 카테고리 이름
     * @param excludeSlug 제외할 포스트의 slug (현재 보고 있는 포스트)
     * @param limit 최대 조회 개수
     * @return 관련 포스트 리스트
     */
    List<Post> findByCategoryExcludingSlug(String category, String excludeSlug, int limit);
}
