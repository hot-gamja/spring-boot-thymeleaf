# 테마 시스템 & 코드블록 스타일 정리

---

## 1. 테마 시스템 구조

### 전제 개념

**다크모드가 동작하는 원리**

브라우저는 `<html>` 태그에 `.dark` 클래스가 붙어 있는지 여부로 테마를 결정한다.

```html
<!-- 라이트 모드 -->
<html>

<!-- 다크 모드 -->
<html class="dark">
```

CSS에서는 두 상태에 대해 색상을 각각 정의해 둔다.

```css
body { background: #ffffff; }        /* 기본(라이트) */
.dark body { background: #111111; }  /* .dark 클래스가 있을 때 */
```

JavaScript가 `<html>`에 `.dark` 클래스를 붙이거나 떼는 것만으로 전체 페이지 색상이 바뀐다.

**왜 3가지인가**

과거에는 라이트 ↔ 다크 두 가지만 있었다. 문제는 OS(Windows, macOS)도 자체 다크모드 설정이 있는데, 웹사이트 설정과 따로 놀면 사용자가 매번 두 군데를 바꿔야 했다. 그래서 "내 OS 설정 그냥 따라가" 옵션이 필요해졌다. 이게 `system` 모드다.

| 모드 | 동작 | 사용 시나리오 |
|------|------|-------------|
| `system` | OS 다크모드 설정 자동 추종 | "신경 쓰기 싫다. OS랑 같으면 된다." |
| `light` | 밝은 테마 고정 | "OS는 다크인데 이 사이트는 밝게 보고 싶다." |
| `dark` | 어두운 테마 고정 | "OS는 라이트인데 이 사이트는 어둡게 보고 싶다." |

기본값은 `system`이다.

**`matchMedia` / `mql.matches` — OS 다크모드 감지**

JavaScript에서 OS의 다크모드 설정을 읽는 방법이다.

```js
var mql = window.matchMedia('(prefers-color-scheme: dark)');
mql.matches  // → true (OS 다크모드) / false (OS 라이트모드)
```

`system` 모드일 때 이 값을 보고 실제 적용할 테마를 결정한다. CSS의 `@media (prefers-color-scheme: dark)` 와 동일한 정보를 JS에서 읽는 것이다.

**순환 방식과 localStorage**

버튼을 클릭할 때마다 다음 순서로 바뀐다.

```
system → light → dark → system → light → ...
```

현재 상태는 localStorage에 저장한다. localStorage는 브라우저에 데이터를 영구 저장하는 공간으로, 탭을 닫아도 유지된다. 다음에 페이지를 열면 마지막 설정을 복원한다.

```js
localStorage.setItem('theme', 'dark');  // 저장
localStorage.getItem('theme');          // → 'dark'
```

순환 구현은 배열과 나머지 연산(`%`)을 조합한다. `current`는 localStorage에서 읽어온 현재 테마 값이다.

```js
var CYCLE = ['system', 'light', 'dark'];
var current = localStorage.getItem('theme') || 'system';  // 저장된 값 또는 기본값

// 현재 인덱스에서 다음으로 이동. 배열 끝을 넘으면 0으로 돌아옴
var next = CYCLE[(CYCLE.indexOf(current) + 1) % CYCLE.length];
```

**아이콘 표시 방식**

각 모드에 맞는 아이콘을 미리 HTML에 3개 넣어 두고, 현재 모드가 아닌 것들은 숨긴다. `hidden`은 `display: none`과 동일한 HTML 표준 속성으로, 요소를 화면에서 완전히 숨긴다.

```html
<button>
  <span id="iconSystem">⚙️</span>
  <span id="iconLight" hidden>☀️</span>  <!-- 숨겨진 상태 -->
  <span id="iconDark"  hidden>🌙</span>  <!-- 숨겨진 상태 -->
</button>
```

모드가 바뀌면 JS로 해당 아이콘의 `hidden` 속성을 붙이거나 제거해서 하나씩만 보이게 한다.

### 전체 흐름 요약

```
페이지 최초 로드
        ↓
localStorage에서 저장된 테마 읽기 (없으면 'system' 기본값)
        ↓
system이면 → OS 다크모드 여부 확인 (mql.matches)
        ↓
<html class="dark"> 붙이거나 제거
        ↓
Tailwind CSS 다크 규칙 활성/비활성
        ↓
버튼 클릭 → 다음 모드로 순환 → localStorage 저장 → 다시 적용
```

---

## 2. 이중 테마 시스템 → 통합

### 전제 개념

**Tailwind 다크모드 — `class` 전략**

Tailwind에서 다크모드를 적용하는 방법은 두 가지다.

```js
// tailwind.config.js
darkMode: 'media'  // OS 설정만 따름. JS로 제어 불가
darkMode: 'class'  // <html>에 .dark 클래스가 있을 때만 다크 스타일 적용
```

이 프로젝트는 `'class'` 전략을 사용한다. JS에서 `.dark` 클래스를 붙였다 떼는 방식으로 테마를 제어한다.

```css
/* Tailwind가 생성하는 규칙 예시 */
.bg-white       { background: #fff; }
.dark .bg-white { background: #111; }  /* .dark 클래스가 있을 때만 */
```

**`classList.toggle(클래스명, 조건)`**

조건이 `true`면 클래스를 추가하고, `false`면 제거한다.

```js
// isDark가 true면 .dark 추가, false면 제거
document.documentElement.classList.toggle('dark', isDark);
```

**`data-*` 속성**

HTML 표준에서 개발자가 임의 데이터를 태그에 붙일 수 있도록 만든 속성이다. CSS에서 이 속성 값으로 스타일을 분기할 수 있다.

```html
<html data-theme="dark">
```

```css
[data-theme="light"] { --bg: #fff; }
[data-theme="dark"]  { --bg: #111; }
```

Tailwind는 `class` 방식을, `blog-base.css`는 `data-theme` 방식을 쓰고 있어서 두 가지를 동시에 설정해야 했다.

**`prefers-color-scheme` — CSS에서 OS 다크모드 감지**

CSS 미디어 쿼리로 OS의 다크모드 설정에 따라 스타일을 분기할 수 있다. JS의 `mql.matches`와 같은 정보를 CSS에서 읽는 방식이다. (→ 섹션 1 참고)

```css
@media (prefers-color-scheme: dark) {
  /* OS가 다크모드일 때 적용 */
}
```

### 통합 전 문제

두 JS 파일이 각자 독립적으로 테마를 관리했다.

| 구분 | shell.js | reader-controls.js |
|------|----------|--------------------|
| 적용 방식 | `.dark` 클래스 | `data-theme="dark"` 속성 |
| localStorage 키 | `theme` | `blog-theme` |
| 지원 모드 | system / light / dark | light / dark |

헤더에서 라이트 모드로 바꾸면 `shell.js`만 반응하고 `reader-controls.js`는 모른다. 결과적으로 헤더는 밝은데 블로그 본문은 어두운 상태가 유지됐다.

### 통합 후 — shell.js 확장

`applyTheme()` 함수 하나에서 두 방식을 동시에 처리한다.

```js
function applyTheme(theme) {
  // OS 다크모드 여부 확인
  var systemDark = mql.matches;

  // system이면 OS 설정 따름, 아니면 명시적 선택 따름
  var isDark = (theme === 'dark') || (theme === 'system' && systemDark);

  // Tailwind용: <html>에 .dark 클래스 붙이거나 떼기
  document.documentElement.classList.toggle('dark', isDark);

  // 커스텀 CSS용: data-theme 속성도 동기화
  document.documentElement.setAttribute('data-theme', isDark ? 'dark' : 'light');
}
```

결과: `<html class="dark" data-theme="dark">` — 두 방식이 항상 일치한다.

`reader-controls.js`에서 테마 관련 코드는 제거하고, **폰트 크기 / 뷰 모드 / 스크롤 진행률**만 남겼다.

---

## 3. highlight.js 자동 전환 원리

### 전제 개념

**DOM (Document Object Model)**

브라우저가 HTML을 파싱해서 만든 트리 구조. JavaScript는 이 트리를 통해 HTML 요소를 읽거나 수정한다.

```
document
└── html  ← document.documentElement
    ├── head
    └── body
```

DOM에서 특정 요소를 찾는 주요 방법들:

```js
document.documentElement          // <html> 태그 (트리 최상위)
document.getElementById('foo')    // id="foo" 인 요소 하나
document.querySelector('.bar')    // CSS 선택자로 첫 번째 요소
```

**`<link disabled>`**

CSS 파일을 불러오되 비활성화할 수 있다. `disabled = true`이면 스타일이 적용되지 않는다. 둘 다 다운로드는 되어 있고, 어느 쪽을 켤지만 JS로 제어한다.

```html
<link id="hljsLight" href="github.min.css">               <!-- 활성화 -->
<link id="hljsDark"  href="github-dark.min.css" disabled>  <!-- 비활성화 -->
```

**`classList` 주요 메서드**

```js
el.classList.toggle('dark', true)    // 조건 true면 추가, false면 제거
el.classList.contains('dark')        // .dark 클래스가 있으면 true, 없으면 false
el.classList.add('dark')             // 추가
el.classList.remove('dark')          // 제거
```

**MutationObserver**

DOM의 변화를 **실시간으로 감시**하는 브라우저 내장 API. 이벤트처럼 "변화가 생기면 콜백 실행"하는 패턴이다.

```js
const observer = new MutationObserver(function(mutations) {
  // 변화가 감지될 때마다 실행되는 콜백
});

observer.observe(감시할_요소, {
  attributes: true,          // 속성 변화 감시
  attributeFilter: ['class'] // 그 중 class 속성만
});
```

테마 버튼 클릭 이벤트를 직접 듣는 방식은 `shell.js`와 `post-detail.html`이 서로를 알아야 한다(강한 결합). MutationObserver는 DOM 상태 자체를 감시하므로 **누가 `.dark` 클래스를 바꿨든 상관없이** 반응한다.

### 실제 코드

```js
// 페이지 로드 시 초기 상태 반영
var isDark  = document.documentElement.classList.contains('dark');
var lightEl = document.getElementById('hljsLight');
var darkEl  = document.getElementById('hljsDark');

if (lightEl && darkEl) {
  lightEl.disabled = isDark;   // 다크면 라이트 테마 꺼짐
  darkEl.disabled  = !isDark;  // 라이트면 다크 테마 꺼짐
}

// 이후 테마 변경 감지
new MutationObserver(function () {
  var dark = document.documentElement.classList.contains('dark');
  if (lightEl) lightEl.disabled = dark;
  if (darkEl)  darkEl.disabled  = !dark;
}).observe(document.documentElement, {
  attributes: true,
  attributeFilter: ['class']  // class 속성 변화만 감시
});
```

**구 reader-controls.js가 왜 안 됐나**

```js
// reader-controls.js (삭제된 코드)
document.documentElement.setAttribute('data-theme', theme);
// class가 아니라 data-theme 속성을 바꿨음
// attributeFilter: ['class'] 에 걸리지 않아 Observer가 반응 안 함
```

Observer는 `class`만 감시하고 있었으므로, `data-theme`가 바뀌어도 콜백이 실행되지 않았다.

### 전체 흐름 요약

```
사용자가 테마 버튼 클릭
        ↓
shell.js: classList.toggle('dark', isDark)
        ↓
<html class="dark"> 로 변경됨
        ↓
MutationObserver 콜백 실행 (class 속성이 바뀌었으므로)
        ↓
.dark 있으면 → lightEl.disabled = true,  darkEl.disabled = false
.dark 없으면 → lightEl.disabled = false, darkEl.disabled = true
        ↓
코드블록 테마 자동 전환
```

---

## 4. 코드블록 CSS 수정 (GitHub 스타일)

### 전제 개념

**`<pre>` vs `<code>`**

마크다운 코드블록은 HTML로 변환되면 항상 이 구조가 된다. highlight.js가 적용되면 `<code>`에 `hljs` 클래스가 붙는다.

```html
<pre>                              <!-- 바깥 박스: 배경, 테두리, 스크롤 -->
  <code class="hljs language-java"> <!-- 안쪽 텍스트: 글자 색상 -->
    public class Foo { ... }
  </code>
</pre>
```

**CSS 변수 (Custom Properties)**

`--`로 시작하는 이름으로 값을 저장해 두고, `var()`로 꺼내 쓰는 방식. 테마 색상처럼 여러 곳에서 쓰이는 값을 한 곳에서 관리할 수 있다.

```css
/* 정의 — :root는 문서 최상위(html 태그)를 의미 */
:root {
  --color-code-bg: #f6f8fa;
}

/* 사용 */
background: var(--color-code-bg);  /* → #f6f8fa */
```

다크 모드에서는 `.dark` 클래스가 `<html>`에 붙으므로, 같은 변수 이름을 다른 값으로 재정의한다. JS로 별도 처리 없이 테마만 바꾸면 색상이 자동으로 바뀐다.

```css
.dark {
  --color-code-bg: #161b22;  /* 같은 변수, 다른 값 */
}
```

**CSS 우선순위 (Specificity)**

같은 요소에 여러 CSS 규칙이 겹칠 때 어느 쪽을 적용할지 결정하는 규칙. 점수가 높을수록 이긴다.

| 선택자 | 예시 | 점수 |
|--------|------|------|
| 인라인 스타일 | `style="..."` | 1000 |
| ID | `#foo` | 100 |
| 클래스 / 속성 | `.foo`, `[type]` | 10 |
| 태그 | `div`, `pre` | 1 |
| `:where()` | `:where(pre)` | 0 |

- **`:where(pre)`** — 태그 선택자지만 점수가 0이다. 나중에 다른 규칙이 쉽게 덮어쓸 수 있도록 의도적으로 낮은 우선순위를 부여한다. Tailwind가 내부적으로 많이 쓰는 패턴이다.
- **`!important`** — 우선순위 계산을 무시하고 강제 적용. 남발하면 나중에 덮어쓰기가 불가능해지므로 최후의 수단으로만 써야 한다.

### 원인

highlight.js 테마 파일은 `<code>`에 배경색을 직접 지정한다.

```css
/* github-dark.min.css */
.hljs {
  background: #0d1117;  /* 자체 배경색 */
  color: #c9d1d9;
}
```

기존 `app.css`는 이 배경이 Tailwind의 회색 배경과 겹치는 걸 막으려고 `!important`로 아예 제거했다.

`:has(> code.hljs)`는 "자식 중에 `code.hljs`가 있는 요소"를 선택하는 CSS 선택자다.
`unset`은 해당 속성을 부모로부터 상속받거나, 상속 불가능한 속성이면 초기값으로 되돌리는 값이다.

```css
/* 기존 app.css — 문제의 규칙 */
.reading .prose :where(pre):has(> code.hljs) {
  background-color: unset !important;  /* hljs 배경을 죽임 */
}
```

결과: `<pre>`에도 배경 없고, `<code>`에도 배경 없어서 박스가 아예 사라진 상태.

### 수정 방향

역할을 명확히 분리한다.

- **박스** (배경, 테두리) → `<pre>`가 담당, CSS 변수로 테마별 색상 적용
- **글자** (syntax highlighting) → `<code>`가 담당, highlight.js 테마가 처리
- `<code>`의 배경은 `transparent`로 비워서 `<pre>` 배경이 비쳐 보이게 한다

### 수정된 CSS

```css
/* CSS 변수 — GitHub README 색상 */
:root {
  --color-code-bg: #f6f8fa;
  --color-code-border: #d0d7de;
}
.dark {
  --color-code-bg: #161b22;
  --color-code-border: #30363d;
}

/* pre: 박스 역할 */
.reading .prose :where(pre) {
  background: var(--color-code-bg);
  border: 1px solid var(--color-code-border);
}

/* code: 배경을 비워서 pre 배경이 보이도록 */
/* highlight.js가 code에 배경을 직접 지정하므로 !important 필요 */
.reading .prose :where(pre) code.hljs {
  background: transparent !important;
}
```

### 전체 흐름 요약

```
테마가 dark로 변경
        ↓
<html class="dark"> 추가됨
        ↓
.dark { --color-code-bg: #161b22 } 활성화
        ↓
pre { background: var(--color-code-bg) } → #161b22 적용
        ↓
code.hljs { background: transparent } → pre 배경이 비침
        ↓
MutationObserver → darkEl CSS 활성화 → 글자 색상 전환
```

---

## 5. 주의사항

| 항목 | 내용 |
|------|------|
| CSS 캐시 | 수정 후 반영이 안 될 경우 `Ctrl+Shift+R` 하드 리프레시. 그래도 안 되면 Spring Boot 재시작 |
| `!important` | `code.hljs`의 `background: transparent` 한 곳에서만 사용 |
| 적용 범위 | `.reading .prose`로 스코핑 → 블로그 포스트에만 영향 |
| Tailwind 업데이트 | `prose` 규칙이 다시 충돌할 수 있으므로 업데이트 후 재확인 필요 |
