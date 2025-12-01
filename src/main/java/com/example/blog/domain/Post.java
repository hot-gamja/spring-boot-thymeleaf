package com.example.blog.domain;

import java.time.LocalDate;
import java.util.List;

/**
 * 블로그 포스트를 나타내는 도메인 클래스
 *
 * 이 클래스는 기술 블로그의 글 하나를 표현합니다.
 * 현재는 POJO로 구현되어 있지만, 나중에 JPA 엔티티로 변환 가능합니다.
 * (그때는 @Entity, @Id 등의 어노테이션을 추가하면 됩니다)
 */
public class Post {

    /**
     * 포스트의 고유 식별자 (URL에 사용)
     * 예: "spring-docker-setup", "postgresql-local-env"
     *
     * 이 값은 URL에 표시되므로 영문/숫자/하이픈만 사용합니다.
     */
    private String slug;

    /**
     * 포스트 제목
     * 예: "Spring Boot와 Docker로 로컬 개발 환경 구축하기"
     */
    private String title;

    /**
     * 포스트 요약/설명 (카드에서 미리보기로 표시)
     * 예: "Docker Compose를 활용해 Spring Boot 애플리케이션을 컨테이너로 실행하는 방법을 알아봅니다."
     */
    private String description;

    /**
     * 포스트 본문 내용 (HTML 포함 가능)
     *
     * 현재는 단순 문자열로 저장하지만,
     * 실제로는 Markdown을 파싱하거나 에디터에서 입력받은 HTML을 저장합니다.
     */
    private String content;

    /**
     * 카테고리 (대분류)
     * 예: "SPRING", "DOCKER", "ALGORITHM", "DATABASE"
     *
     * 하나의 포스트는 하나의 카테고리에만 속합니다.
     */
    private String category;

    /**
     * 태그 목록 (소분류, 여러 개 가능)
     * 예: ["Java", "Spring Boot", "Docker Compose"]
     *
     * 태그는 카테고리보다 세부적인 분류로, 여러 개를 가질 수 있습니다.
     */
    private List<String> tags;

    /**
     * 작성 날짜
     * 예: 2025-01-15
     */
    private LocalDate date;

    /**
     * 썸네일 이미지 URL (선택 사항)
     * 예: "/images/posts/spring-docker-thumbnail.png"
     *
     * null이면 썸네일 없이 표시됩니다.
     */
    private String thumbnailUrl;

    /**
     * 포스트 요약 (본문 하단에 표시할 핵심 요약)
     * 예: "이 글에서는 Docker Compose로 Spring Boot 앱을 실행하고,
     *      PostgreSQL과 연동하는 방법을 다뤘습니다."
     *
     * description과의 차이:
     * - description: 카드 미리보기용 (짧음)
     * - summary: 글 하단 요약용 (좀 더 자세함)
     */
    private String summary;

    // ========== 생성자 ==========

    /**
     * 기본 생성자
     */
    public Post() {
    }

    /**
     * 전체 필드를 받는 생성자 (더미 데이터 생성 시 편리)
     */
    public Post(String slug, String title, String description, String content,
                String category, List<String> tags, LocalDate date,
                String thumbnailUrl, String summary) {
        this.slug = slug;
        this.title = title;
        this.description = description;
        this.content = content;
        this.category = category;
        this.tags = tags;
        this.date = date;
        this.thumbnailUrl = thumbnailUrl;
        this.summary = summary;
    }

    // ========== Getter / Setter ==========

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    // ========== toString (디버깅용) ==========

    @Override
    public String toString() {
        return "Post{" +
                "slug='" + slug + '\'' +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", date=" + date +
                '}';
    }
}
