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

## 2. 글쓰기 기능

### 글쓰기

- **`ArticleContoller`**
  - `@Controller` 매핑
  - 글쓰기 작성 요청
    - `POST` + URL 매핑
  - `Article` Entity 추가
  - `List<Article>` 로 글 목록 관리
  - 글쓰기 완료 후 페이지로 리다이렉션
    - `redirect:/` 사용

### 글 목록 조회

- 메인 페이지에서 게시글 목록을 조회
- 목록을 `Model`에 저장한 후에 `View`로 전달
  - `List<Article>`그대로 전달한다

### 게시글 상세보기

- 게시글 목록의 **제목을 클릭**했을 때 게시글 상세 페이지로 접속
  - `<a href="/articles/{{index}}" />`로 구현
  - 게시글 객체에 id 필드 추가
  - `List<Article>`에 추가할 때 `articles.size() + 1`로 id 부여
- `articles`에서 `index - 1`로 데이터를 조회 후 `View`로 전달

### 회원정보 수정 화면

- `user/{id}/form` 로 접근
- 업데이트 form을 통해 사용자 정보 수정

### 회원정보 수정

- `/users/{id}/update` 로 접근
- `{id}`에 해당하는 User를 `UserRepository`에서 조회
- `User` 객체를 수정
- 수정된 `User` 객체를 `UserRepository`에 저장
- 사용자 정보 수정 후 `redirect:/users`로 리다이렉션

### PUT method

- `PUT` method를 사용하여 사용자 정보 수정
- `@PutMapping`을 사용하여 URL 매핑
- `<input type="hidden" name="_method" value="PUT" />`을 사용하여 `PUT` method로 요청
- Spring에서 `PUT` method를 사용하기 위해 `HiddenHttpMethodFilter`를 추가
  - `spring.mvc.hiddenmethod.filter.enabled=true` 추가

## 3. DB에 저장하기

### H2 DB 설정

- Spring Data JPA를 사용하여 H2 DB를 사용
- 의존성 추가

### 게시글 데이터 저장

- `ArticleRepository`를 사용하여 게시글 데이터를 저장
- `Article` Entity에 `@Entity` 어노테이션 추가
- `Article` Entity에 `@Id` 어노테이션 추가
  - `PK`로 지정

### 게시글 목록 구현

- `ArticleRepository`를 사용하여 게시글 목록을 조회
  - `findAll()` 메소드 사용

### 상세보기 구현

- `ArticleRepository`를 사용하여 게시글 상세보기 구현
  - `findById()` 메소드 사용

### 사용자 데이터 저장

- `UserRepository`를 사용하여 사용자 데이터를 저장
  - `save()` 메소드 사용

### 배포

https://port-0-be-spring-cafe-m8famrv1da7fb95d.sel4.cloudtype.app/
