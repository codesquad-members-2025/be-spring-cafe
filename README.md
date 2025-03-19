# be-spring-cafe

2025 마스터즈 백엔드 스프링 카페

### 배포 링크
    https://port-0-be-cafe2-m85rwv28724587d4.sel4.cloudtype.app/

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

### 트러블슈팅
- spring data jpa를 연동하고 @Entity를 사용한 후 @ModelAttribute를 사용한 바인딩이 동작하지 않게 되었다.
  @Entity를 추가하며 기본 생상자를 추가했는데 스프링이 기본생성자를 우선해서 객체를 생성하고 이로 인해 기존에 작성해놨던 생성자가 동작하지 않아 발생한 문제였다. 
 setter를 추가해서 기본생성자를 이용하는 방법도 있겠지만 setter를 사용하기는 싫어서 UserDto를 만든 후 이를 User객체로 변환 후 db에 저장했다.
- jpa를 적용후 User테이블이 생기지 않는 문제가 발생했다. 원인은 h2가 user를 이미 예약어로 사용하고 있기 때문에 같은 이름의 테이블을 생성하지 못하는 것이었다.
 @Table의 name속성을 이용해 users로 테이블 이름을 바꾸니 잘 생성 되었다.