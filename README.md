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
- 회원 정보를 수정시 비밀번호를 검증한다.
- 비밀번호가 틀린 경우 에러 메세지를 띄우고 다시 입력하도록 한다.
- 검증이 성공한 경우 정보를 수정가능한 폼을 띄운다.

### 3단계
- H2 DB를 연동한다.
- Spring Data Jpa를 이용하여 DB를 조회한다.
- service와 repository를 구현하여 컨트롤러와 로직을 분리한다.
- DB에 저장하기위해 각 엔티티에 ID를 설정한다.
- 게시글과 회원간 연관관계를 설장한다 회원 : 1 - N : 게시글