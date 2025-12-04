당신은 시니어 프론트엔드 + 백엔드 개발자입니다.
아래 조건을 만족하는 코드를 작성하세요.

[프로젝트 정보]
- 스택: Spring Boot + Thymeleaf + Vanilla JS + CSS
- 템플릿 엔진: Thymeleaf
- 언어: Java 기반 서버 + HTML/CSS/JS
- 내가 코드를 붙여넣을 위치:
  - 템플릿: src/main/resources/templates
  - 프래그먼트: src/main/resources/templates/fragments
  - 레이아웃: src/main/resources/templates/layout
  - CSS: src/main/resources/static/css

[현재 상황]
- 나는 지금 "헤더 네비게이션바 + 왼쪽 사이드바" 레이아웃을 임시로 구현하려고 합니다.
- 깃 브랜치 이름: feat/layout-sidebar-navbar
- 현재는 기본 페이지 템플릿 정도만 있고, 상단 헤더/네비게이션과 왼쪽 사이드바를 추가해서 기술 블로그 레이아웃 골격을 만들고 싶습니다.

[요구사항 - 레이아웃]
1. 상단 헤더 영역
   - 왼쪽: 햄버거 메뉴 아이콘 (3줄짜리 아이콘)
   - 오른쪽: 텍스트 로고 "HotGamja Lab"
   - 오른쪽: 상단 네비게이션 메뉴
     - 메뉴 예시: 홈(/), 게시글(/posts), 소개(/about) , 로그인(/login)
   - 화면 전체 너비를 차지하는 다크톤의 상단 바로 구현

2. 왼쪽 사이드바
   - 페이지 왼쪽에 고정된 세로 네비게이션
   - 스크롤 시 따라오지는 않음
   - 평소에는 접혀있다가, 햄버거 메뉴 아이콘 클릭 시 펼쳐지도록 구현 (간단한 JavaScript로 토글 기능)
   - 메뉴 예시:
     - 홈(/)
     - 소개(/about)
     - 게시글(/blog)
     - photo gallery(/gallery)
   - 사이드바는 약 220px 정도 너비, 연한 회색 배경, 오른쪽에 얇은 보더

3. 메인 콘텐츠 영역
   - 헤더 아래, 사이드바 오른쪽에 위치
   - 임시로 "최신 글" 제목과 더미 포스트 2개 정도만 표시
   - 이 부분은 나중에 실제 게시글 리스트 템플릿으로 바꿀 예정이므로, 구조만 잡아두면 됩니다.

4. 푸터
    - 페이지 맨 아래에 고정
    - 간단한 저작권 문구만 표시 (예: © 2024 HotGamja Lab)

[Thymeleaf / 파일 구조 요구사항]
- 아래와 같이 파일을 분리해 주세요.
  - templates/layout/layout.html  : 전체 레이아웃 (공통 틀)
  - templates/fragments/header.html : 헤더 fragment (로고 + 상단 네비게이션)
  - templates/fragments/sidebar.html : 왼쪽 사이드바 fragment (카테고리 메뉴)
- layout.html 안에서 th:replace를 사용해 header와 sidebar fragment를 불러오도록 해주세요.
- 메인 콘텐츠는 layout.html 안에 `<main>` 영역으로 두고, 안쪽 일부를 th:fragment로 만들어서 다른 템플릿에서 확장해서 사용할 수 있는 예시도 같이 보여주면 좋겠습니다.

[스타일링 요구사항]
- CSS는 src/main/resources/static/css/layout.css 파일 하나로 작성해 주세요.
- 레이아웃은 flex를 사용해 구현:
  - 헤더는 상단에 고정(페이지 위쪽에 full width)
  - 헤더 아래에 `sidebar + content` 가 가로로 나란히 배치
- 색상:
  - 헤더 배경: 진한 남색 또는 거의 검정 (#111827 계열)
  - 헤더 텍스트: 흰색
  - 사이드바 배경: 연한 회색 (#f3f4f6 계열)
  - 콘텐츠 배경은 기본 흰색
- 반응형까지 완벽하게 할 필요는 없지만, 최소한 1024px 너비 기준으로 보기 좋게 맞춰 주세요.

[코드 출력 형식]
1. layout.html 전체 코드
2. fragments/header.html 전체 코드
3. fragments/sidebar.html 전체 코드
4. layout.css 전체 코드
5. 각 파일에 대해 한국어로 역할 설명 + 레이아웃 포인트 간단히 설명

[추가 주의사항]
- React, Vue, jQuery, TypeScript 등은 사용하지 마세요.
- 순수 HTML + CSS + JS만 사용하세요.
- 주석과 설명은 한국어로 작성해 주세요.
- 실제로 내가 바로 프로젝트에 붙여넣고 빌드해서 볼 수 있을 정도로 완전한 코드로 작성해 주세요.

