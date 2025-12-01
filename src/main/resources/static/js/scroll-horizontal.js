/**
 * scroll-horizontal.js
 *
 * 역할:
 * - 포스트 리스트에서 마우스 휠을 사용하면 가로 스크롤
 * - dayloog.com 스타일의 "Drag or Scroll" UX
 * - 드래그로도 스크롤 가능
 *
 * 특징:
 * - PC에서만 작동 (모바일은 기본 세로 스크롤)
 * - 부드러운 스크롤 애니메이션
 * - 마우스 커서 변경 (드래그 중)
 *
 * 사용 방법:
 * - HTML에서 <script src="/js/scroll-horizontal.js"></script>로 로드
 * - .post-list 요소가 있으면 자동으로 적용
 */

(function() {
    'use strict';

    // ========== 설정 ==========
    const SCROLL_SPEED = 1.5; // 마우스 휠 스크롤 속도 배율
    const DRAG_THRESHOLD = 5;  // 드래그로 인식하는 최소 이동 거리 (px)

    // ========== DOM 요소 참조 ==========
    let postList = null;

    // ========== 드래그 상태 ==========
    let isDragging = false;
    let startX = 0;
    let scrollLeft = 0;

    /**
     * 초기화 함수
     */
    function init() {
        // .post-list 요소 찾기
        postList = document.querySelector('.post-list');

        if (!postList) {
            // 포스트 리스트가 없으면 초기화 안 함 (예: 상세 페이지)
            return;
        }

        // 모바일인지 확인
        if (isMobileDevice()) {
            console.log('📱 모바일 기기: 가로 스크롤 비활성화');
            return;
        }

        // 이벤트 리스너 등록
        registerWheelScroll();
        registerDragScroll();

        console.log('✅ 가로 스크롤 초기화 완료');
    }

    /**
     * 모바일 기기인지 확인
     *
     * @returns {boolean} 모바일이면 true
     */
    function isMobileDevice() {
        // 1. User Agent로 확인
        const userAgent = navigator.userAgent.toLowerCase();
        const mobileKeywords = ['mobile', 'android', 'iphone', 'ipad', 'ipod'];
        const isMobileUA = mobileKeywords.some(keyword => userAgent.includes(keyword));

        // 2. 터치 지원 여부 확인
        const hasTouch = 'ontouchstart' in window || navigator.maxTouchPoints > 0;

        // 3. 화면 너비 확인 (768px 이하면 모바일로 간주)
        const isSmallScreen = window.innerWidth <= 768;

        return isMobileUA || (hasTouch && isSmallScreen);
    }

    /**
     * 마우스 휠로 가로 스크롤
     *
     * 동작:
     * - 마우스 휠을 위/아래로 움직이면
     * - .post-list가 좌/우로 스크롤됨
     */
    function registerWheelScroll() {
        postList.addEventListener('wheel', (e) => {
            // 세로 스크롤이 아니면 무시
            if (e.deltaY === 0) return;

            // View 모드가 "image"면 가로 스크롤 비활성화 (그리드 레이아웃이므로)
            const viewMode = postList.getAttribute('data-view');
            if (viewMode === 'image') return;

            // 기본 스크롤 동작 막기
            e.preventDefault();

            // 가로 스크롤 실행
            // deltaY: 양수면 아래로, 음수면 위로
            // 아래로 휠 -> 오른쪽 스크롤, 위로 휠 -> 왼쪽 스크롤
            const scrollAmount = e.deltaY * SCROLL_SPEED;
            postList.scrollLeft += scrollAmount;
        }, { passive: false }); // passive: false로 preventDefault 허용
    }

    /**
     * 드래그로 스크롤
     *
     * 동작:
     * - 마우스 클릭 + 드래그하면 스크롤
     * - 드래그 중 커서가 "grabbing"으로 변경
     */
    function registerDragScroll() {
        // View 모드가 "image"면 드래그 스크롤 비활성화
        const viewMode = postList.getAttribute('data-view');
        if (viewMode === 'image') return;

        // 마우스 다운: 드래그 시작
        postList.addEventListener('mousedown', (e) => {
            isDragging = true;
            startX = e.pageX - postList.offsetLeft;
            scrollLeft = postList.scrollLeft;

            // 커서 변경
            postList.style.cursor = 'grabbing';
            postList.style.userSelect = 'none'; // 텍스트 선택 방지

            // 이벤트 전파 방지 (링크 클릭 등)
            // 단, 이동 거리가 DRAG_THRESHOLD 이상일 때만 링크 클릭 방지
        });

        // 마우스 이동: 스크롤 업데이트
        postList.addEventListener('mousemove', (e) => {
            if (!isDragging) return;

            e.preventDefault();

            const x = e.pageX - postList.offsetLeft;
            const walk = (x - startX) * 2; // 이동 거리 증폭 (더 빠르게)

            postList.scrollLeft = scrollLeft - walk;
        });

        // 마우스 업: 드래그 종료
        postList.addEventListener('mouseup', (e) => {
            if (!isDragging) return;

            // 이동 거리 계산
            const x = e.pageX - postList.offsetLeft;
            const distance = Math.abs(x - startX);

            // 이동 거리가 작으면 클릭으로 간주 (링크 클릭 허용)
            if (distance < DRAG_THRESHOLD) {
                // 아무것도 안 함 (기본 클릭 동작 허용)
            } else {
                // 드래그로 간주 (링크 클릭 방지)
                e.preventDefault();
            }

            isDragging = false;

            // 커서 복원
            postList.style.cursor = 'grab';
            postList.style.userSelect = '';
        });

        // 마우스 리브: 드래그 종료 (마우스가 영역 밖으로 나감)
        postList.addEventListener('mouseleave', () => {
            if (isDragging) {
                isDragging = false;
                postList.style.cursor = 'grab';
                postList.style.userSelect = '';
            }
        });

        // 초기 커서 설정
        postList.style.cursor = 'grab';
    }

    /**
     * View 모드 변경 감지
     *
     * Reader Controls에서 View 모드를 변경하면
     * 가로 스크롤 활성화/비활성화
     */
    function watchViewModeChange() {
        if (!postList) return;

        // MutationObserver로 data-view 속성 변경 감지
        const observer = new MutationObserver((mutations) => {
            mutations.forEach((mutation) => {
                if (mutation.type === 'attributes' && mutation.attributeName === 'data-view') {
                    const viewMode = postList.getAttribute('data-view');

                    if (viewMode === 'image') {
                        // 이미지 모드: 드래그 커서 제거
                        postList.style.cursor = '';
                        console.log('🖼️ 이미지 모드: 가로 스크롤 비활성화');
                    } else {
                        // 기본 모드: 드래그 커서 추가
                        postList.style.cursor = 'grab';
                        console.log('📄 기본 모드: 가로 스크롤 활성화');
                    }
                }
            });
        });

        observer.observe(postList, {
            attributes: true,
            attributeFilter: ['data-view']
        });
    }

    /**
     * 스크롤 인디케이터 추가 (선택 사항)
     *
     * 포스트 리스트 하단에 스크롤 위치 표시
     * (현재는 구현하지 않음, 필요하면 추가)
     */
    function addScrollIndicator() {
        // TODO: 스크롤 인디케이터 구현
        // 예: 작은 점들로 현재 위치 표시
    }

    /**
     * 키보드 화살표로 스크롤 (선택 사항)
     *
     * 좌/우 화살표 키로 스크롤 가능
     */
    function initKeyboardScroll() {
        document.addEventListener('keydown', (e) => {
            if (!postList) return;

            const viewMode = postList.getAttribute('data-view');
            if (viewMode === 'image') return;

            // 포커스가 입력 필드에 있으면 무시
            if (document.activeElement.tagName === 'INPUT' ||
                document.activeElement.tagName === 'TEXTAREA') {
                return;
            }

            const scrollAmount = 300; // 한 번에 스크롤할 거리 (px)

            switch(e.key) {
                case 'ArrowLeft':
                    e.preventDefault();
                    postList.scrollBy({ left: -scrollAmount, behavior: 'smooth' });
                    break;
                case 'ArrowRight':
                    e.preventDefault();
                    postList.scrollBy({ left: scrollAmount, behavior: 'smooth' });
                    break;
            }
        });

        console.log('⌨️ 키보드 스크롤 활성화');
    }

    // ========== 페이지 로드 시 초기화 ==========
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', () => {
            init();
            watchViewModeChange();
            // initKeyboardScroll(); // 원하면 주석 해제
        });
    } else {
        init();
        watchViewModeChange();
        // initKeyboardScroll(); // 원하면 주석 해제
    }

})();

/**
 * 사용 예시:
 *
 * 1. 가로 스크롤 테스트:
 *    - 홈 페이지(/)에서 마우스 휠을 위/아래로 움직이면
 *    - 포스트 카드가 좌/우로 스크롤됨
 *
 * 2. 드래그 스크롤 테스트:
 *    - 포스트 리스트를 클릭 + 드래그
 *    - 커서가 "grabbing"으로 변경되며 스크롤
 *
 * 3. View 모드 전환:
 *    - Reader Controls에서 "Image" 버튼 클릭
 *    - 가로 스크롤이 비활성화되고 그리드 레이아웃으로 변경
 *
 * 4. 디버깅:
 *    - 콘솔에서 경고/로그 확인
 *    - 모바일에서는 "📱 모바일 기기: 가로 스크롤 비활성화" 로그 출력
 */
