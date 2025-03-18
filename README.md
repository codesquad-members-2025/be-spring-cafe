# be-spring-cafe

2025 마스터즈 백엔드 스프링 카페

## 1. 회원 가입 및 목록 조회 기능

### 회원 가입

- `static/user/form.html` -> `templates/user/form.html`
- 사용자 관리 기능 구현 담당 컨트롤러 추가
  - `UserController` + `@Controller` 매핑
- 회원가입 요청 처리
  - `POST` + URL 매핑
- `User` 도메인 추가
    - `id`, `name`, `email`, `password` 필드
    - `getter`, `setter` 추가
- 사용자 목록 관리 `List<User>` 추가
- 사용자 추가 완료 후 페이지로 리다이렉션
  - `redirect:/users` 사용

### 회원목록 기능

- `static/user/list.html` -> `templates/user/list.html`
- `UserCOntroller` 그대로 사용
- 사용자 목록 조회 요청 처리
  - `GET` + URL 매핑
- `Model` 메소드의 파라미터로 전달
  - `users`라는 이름으로 전달
- 사용자 목록을 `user/list.html`로 전달하기 위해 `return user/list`
- `user/list.html`에 사용자 목록 출력

### 회원 프로필 정보

- `static/user/profile.html` -> `templates/user/profile.html`
- `user/list.html`에서 닉네임 클릭시 **프로필 페이지로 이동**
  - `<a />`태그로 이동이 가능
  - `<a href="/users/{{userId}}" />`로 구현
- 회원 프로필 요청 메소드 추가
  - `GET` + URL 매핑
- URL 매핑에 사용자 식별자 추가
  - `@PathVariable` 사용
- `List<User>` 중에서 일치하는 User를 Model에 저장
- `user/profile.html`에 사용자 정보 출력

### HTML 중복 제거

- `index.html`, `/user/form.html`, `/qna/form.html`에 중복된 코드가 존재
