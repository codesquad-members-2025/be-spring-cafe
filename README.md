# be-spring-cafe

2025 마스터즈 백엔드 스프링 카페

### 1단계
- thymeleaf를 사용하여 템플릿 방식으로 구현한다.
- 회원 관리와 관련된 기능은 /users로 통일한다.
- Post /users -  화원가입
- Get /users -  모든 회원 조회
- Get /users/:id - 아이디로 회원 조회

### 2단계
- Post /articles - 글 작성
- Get / - 글 목록 조회
- Get /articles/{id} - 게시글 상세 보기
- Get /users/{id}/form - 회원정보 수정 폼 요청
- Put /users/{id}/update - 회원정보 수정
- 홈 화면에서 작성한 글들을 볼 수 있다.
- 게시글을 클릭하면 상세 화면을 볼 수 있다. 