/**
 * reader-controls.js
 *
 * 역할:
 * - 폰트 크기 조절 (S/M/L)
 * - 테마 전환 (Light/Dark)
 * - 뷰 모드 전환 (Default/Image)
 * - 설정을 localStorage에 저장 (새로고침 후에도 유지)
 * - 스크롤 진행률 표시 (선택 사항)
 *
 * 사용 방법:
 * - HTML에서 <script src="/js/reader-controls.js"></script>로 로드
 * - 페이지 로드 시 자동으로 초기화
 */

(function() {
    'use strict';

    // ========== 상수 정의 ==========
    const STORAGE_KEYS = {
        FONT_SIZE: 'blog-font-size',
        THEME: 'blog-theme',
        VIEW_MODE: 'blog-view-mode'
    };

    const DEFAULT_VALUES = {
        FONT_SIZE: 'M',
        THEME: 'dark',
        VIEW_MODE: 'default'
    };

    // ========== DOM 요소 참조 ==========
    let fontSizeButtons = [];
    let themeButtons = [];
    let viewButtons = [];
    let postList = null;
    let scrollProgressText = null;

    /**
     * 초기화 함수
     * 페이지 로드 시 자동 실행
     */
    function init() {
        // DOM 요소 가져오기
        fontSizeButtons = document.querySelectorAll('[data-font-size]');
        themeButtons = document.querySelectorAll('[data-theme]');
        viewButtons = document.querySelectorAll('[data-view]');
        postList = document.querySelector('.post-list');
        scrollProgressText = document.querySelector('.scroll-progress-text');

        // localStorage에서 저장된 설정 불러오기
        loadSettings();

        // 이벤트 리스너 등록
        registerEventListeners();

        // 스크롤 진행률 초기화 (선택 사항)
        if (scrollProgressText) {
            initScrollProgress();
        }

        console.log('✅ Reader Controls 초기화 완료');
    }

    /**
     * localStorage에서 설정 불러와서 적용
     */
    function loadSettings() {
        // 1. 폰트 크기 불러오기
        const savedFontSize = localStorage.getItem(STORAGE_KEYS.FONT_SIZE) || DEFAULT_VALUES.FONT_SIZE;
        applyFontSize(savedFontSize);

        // 2. 테마 불러오기
        const savedTheme = localStorage.getItem(STORAGE_KEYS.THEME) || DEFAULT_VALUES.THEME;
        applyTheme(savedTheme);

        // 3. 뷰 모드 불러오기
        const savedViewMode = localStorage.getItem(STORAGE_KEYS.VIEW_MODE) || DEFAULT_VALUES.VIEW_MODE;
        applyViewMode(savedViewMode);
    }

    /**
     * 이벤트 리스너 등록
     */
    function registerEventListeners() {
        // 1. 폰트 크기 버튼 클릭
        fontSizeButtons.forEach(button => {
            button.addEventListener('click', () => {
                const size = button.getAttribute('data-font-size');
                applyFontSize(size);
                localStorage.setItem(STORAGE_KEYS.FONT_SIZE, size);
            });
        });

        // 2. 테마 버튼 클릭
        themeButtons.forEach(button => {
            button.addEventListener('click', () => {
                const theme = button.getAttribute('data-theme');
                applyTheme(theme);
                localStorage.setItem(STORAGE_KEYS.THEME, theme);
            });
        });

        // 3. 뷰 모드 버튼 클릭
        viewButtons.forEach(button => {
            button.addEventListener('click', () => {
                const view = button.getAttribute('data-view');
                applyViewMode(view);
                localStorage.setItem(STORAGE_KEYS.VIEW_MODE, view);
            });
        });
    }

    /**
     * 폰트 크기 적용
     *
     * @param {string} size - "S", "M", "L" 중 하나
     *
     * 동작:
     * 1. <html> 태그에 data-font-size 속성 변경
     * 2. CSS에서 [data-font-size="S"] 선택자로 폰트 크기 변경
     * 3. 버튼 active 상태 업데이트
     */
    function applyFontSize(size) {
        // <html> 태그에 속성 설정
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

        console.log(`📏 폰트 크기 변경: ${size}`);
    }

    /**
     * 테마 적용
     *
     * @param {string} theme - "light" 또는 "dark"
     *
     * 동작:
     * 1. <html> 태그에 data-theme 속성 변경
     * 2. CSS 변수가 자동으로 변경됨 (blog-base.css 참고)
     * 3. 버튼 active 상태 업데이트
     */
    function applyTheme(theme) {
        // <html> 태그에 속성 설정
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

        console.log(`🎨 테마 변경: ${theme}`);
    }

    /**
     * 뷰 모드 적용
     *
     * @param {string} view - "default" 또는 "image"
     *
     * 동작:
     * 1. .post-list 요소에 data-view 속성 변경
     * 2. CSS에서 [data-view="default"], [data-view="image"]로 레이아웃 변경
     * 3. 버튼 active 상태 업데이트
     */
    function applyViewMode(view) {
        if (!postList) {
            console.warn('⚠️ .post-list 요소를 찾을 수 없습니다.');
            return;
        }

        // .post-list에 속성 설정
        postList.setAttribute('data-view', view);

        // 버튼 active 상태 업데이트
        viewButtons.forEach(button => {
            const buttonView = button.getAttribute('data-view');
            if (buttonView === view) {
                button.classList.add('active');
            } else {
                button.classList.remove('active');
            }
        });

        console.log(`👁️ 뷰 모드 변경: ${view}`);
    }

    /**
     * 스크롤 진행률 표시 (선택 사항)
     *
     * 동작:
     * - 페이지를 스크롤하면 진행률 % 계산
     * - .scroll-progress-text에 표시
     */
    function initScrollProgress() {
        // 스크롤 이벤트 리스너 (throttle 적용)
        let ticking = false;

        window.addEventListener('scroll', () => {
            if (!ticking) {
                window.requestAnimationFrame(() => {
                    updateScrollProgress();
                    ticking = false;
                });
                ticking = true;
            }
        });

        // 초기값 설정
        updateScrollProgress();
    }

    /**
     * 스크롤 진행률 업데이트
     */
    function updateScrollProgress() {
        if (!scrollProgressText) return;

        // 전체 문서 높이
        const documentHeight = document.documentElement.scrollHeight - window.innerHeight;

        // 현재 스크롤 위치
        const scrollTop = window.pageYOffset || document.documentElement.scrollTop;

        // 진행률 계산 (0 ~ 100%)
        const progress = Math.min(Math.round((scrollTop / documentHeight) * 100), 100);

        // 텍스트 업데이트
        scrollProgressText.textContent = `${progress}%`;
    }

    /**
     * 키보드 단축키 지원 (선택 사항)
     *
     * - Ctrl + 1: 폰트 S
     * - Ctrl + 2: 폰트 M
     * - Ctrl + 3: 폰트 L
     * - Ctrl + D: 다크 테마
     * - Ctrl + L: 라이트 테마
     */
    function initKeyboardShortcuts() {
        document.addEventListener('keydown', (e) => {
            // Ctrl 키가 눌렸는지 확인
            if (!e.ctrlKey && !e.metaKey) return;

            switch(e.key) {
                case '1':
                    e.preventDefault();
                    applyFontSize('S');
                    localStorage.setItem(STORAGE_KEYS.FONT_SIZE, 'S');
                    break;
                case '2':
                    e.preventDefault();
                    applyFontSize('M');
                    localStorage.setItem(STORAGE_KEYS.FONT_SIZE, 'M');
                    break;
                case '3':
                    e.preventDefault();
                    applyFontSize('L');
                    localStorage.setItem(STORAGE_KEYS.FONT_SIZE, 'L');
                    break;
                case 'd':
                case 'D':
                    e.preventDefault();
                    applyTheme('dark');
                    localStorage.setItem(STORAGE_KEYS.THEME, 'dark');
                    break;
                case 'l':
                case 'L':
                    e.preventDefault();
                    applyTheme('light');
                    localStorage.setItem(STORAGE_KEYS.THEME, 'light');
                    break;
            }
        });

        console.log('⌨️ 키보드 단축키 활성화');
    }

    /**
     * 설정 초기화 함수 (디버깅용)
     * 콘솔에서 resetReaderSettings() 실행 시 모든 설정 초기화
     */
    window.resetReaderSettings = function() {
        localStorage.removeItem(STORAGE_KEYS.FONT_SIZE);
        localStorage.removeItem(STORAGE_KEYS.THEME);
        localStorage.removeItem(STORAGE_KEYS.VIEW_MODE);

        applyFontSize(DEFAULT_VALUES.FONT_SIZE);
        applyTheme(DEFAULT_VALUES.THEME);
        applyViewMode(DEFAULT_VALUES.VIEW_MODE);

        console.log('🔄 설정 초기화 완료');
    };

    // ========== 페이지 로드 시 초기화 ==========
    if (document.readyState === 'loading') {
        // DOM이 아직 로드 중이면 DOMContentLoaded 이벤트 대기
        document.addEventListener('DOMContentLoaded', init);
    } else {
        // 이미 로드되었으면 바로 실행
        init();
    }

    // 키보드 단축키 활성화 (선택 사항, 원하지 않으면 주석 처리)
    // initKeyboardShortcuts();

})();

/**
 * 사용 예시:
 *
 * 1. 브라우저 콘솔에서 설정 확인:
 *    localStorage.getItem('blog-font-size')
 *    localStorage.getItem('blog-theme')
 *
 * 2. 설정 초기화:
 *    resetReaderSettings()
 *
 * 3. 프로그래밍 방식으로 변경 (디버깅용):
 *    document.documentElement.setAttribute('data-font-size', 'L')
 *    document.documentElement.setAttribute('data-theme', 'light')
 */
