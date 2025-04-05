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

## 4. 로그인 기능

### 로그인 기능

- `HttpSession`을 사용하여 로그인 기능 구현
  - 로그인 중일 때 상단 메뉴에 `로그아웃`, `개인정보 수정` 버튼이 보이도록 구현
  - 로그인 상태가 아니라면 `로그인`, `회원가입` 버튼이 보이도록 구현

## 5. 게시글 권한 부여

### 게시글 작성

- 로그인 하지 않은 사용자가 글쓰게 페이지에 접근시 **로그인 페이지로 이동**

### 게시글 수정

- 수정하기 폼과 수정하기 버튼은 **작성자와 일치해야만 가능**
- 만약 다를 경우 `"다른 사람의 글을 수정할 수 없다"`는 에러페이지로 이동

### 게시글 삭제

- 삭제하기 버튼은 **작성자와 일치해야만 가능**
- 수정과 마찬가지로 에러페이지 이동
- `@DeleteMapping`
  - html에서 `form`태그의 `method`를 `DELETE`로 지정

### 데이터베이스 모델링

- `Article` Entity에 `User` Entity를 추가
  - `@ManyToOne` 어노테이션을 사용하여 `User` Entity와 관계를 맺음
  - `@JoinColumn` 어노테이션을 사용하여 `User` Entity의 `id`와 매핑

## 6. 댓글

### 댓글 기능 추가

- **로그인 한 사용자**는 게시글 상세보기에서 댓글을 보기 가능
- **로그인 한 사용자**는 댓글 추가 가능
- **자신이 작성한 댓글**은 수정 및 삭제 가능

### 게시글 삭제하기

- 게시글 삭제시 **데이터가 아닌 상태로 삭제**
- 로그인한 사용자와 게시글 작성자가 **같은 경우에 삭제가 가능**
- 게시글 작성자와 댓글 작성자가 **다를 경우 삭제 불가능**
- 게시글 작성자 및 댓글 작성자가 **모두 일치하는 경우에만 삭제 가능**
- 게시글 삭제시 **댓글도 함께 삭제**


## 7. ajax 댓글

### 댓글 기능 ajax로 변경

- `@RestController`를 사용하여 댓글 기능을 ajax로 변경
- 댓글 추가, 삭제 기능을 ajax로 변경
- 추가 및 삭제시 **화면이 리프레시 되지 않도록 구현**

### DB 변경

- `H2` DB에서 `MySQL`로 변경

## 8. 페이징 구현하기

### TDD

- 페이징 구현에 있어 테스트 주도 개발을 통해 구현

### 게시물 페이징 기능 추가

- 게시글을 한 페이지에 **15개씩** 보이도록 구현
- 생성일 기준 **내림차 순**으로 정렬

### 댓글 페이징 기능 추가

- 댓글을 한 페이지에 **5개씩** 보이도록 구현
- 생성일 기준 **내림차 순**으로 정렬
- **더보기** 버튼을 클릭했을 때 댓글이 추가로 보이도록 구현
