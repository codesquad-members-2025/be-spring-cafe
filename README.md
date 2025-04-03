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

### 4단계
- 세션과 쿠키를 이용하여 로그인을 구현한다.
- 로그인 버튼을 누르는 경우 DB에서 회원가입한 회원인지 찾는다.
- 회원가입을 안했다면 오류 메세지를 주고 회원이라면 쿠키에 세션아이디를 등록한다.
- session.setAttribute로 user데이터를 등록해놓는다.
- 로그인이 성공하면 헤더 버튼이 로그아웃, 개인정보 수정으로 변한다.
- 개인정보를 수정하는 경우 로그인을 한 경우만 가능하다.
- 테스트 데이터를 추가하여 로그인 기능 테스트를 쉽게 한다.
- userRepository를 목객체로 사용하여 테스트를 작성한다.

### 5단게
- 로그인하지 않은 사용자가 글쓰기를 누른경우 로그인 페이지로 이동한다.
- 로그인하지 않은 사용자는 게시글을 누른경우 로그인 페이지로 이동한다.
- 게시글을 볼 때 자신의 글만 수정, 삭제 할 수 있다.
- Article이 User의 PK를 외래키로 갖는다.
- 게시글마다 작성자와 작성일자를 추가한다.

### 6단계
- 로그인한 사용자는 게시글 상세보기 화면에서 댓글들을 볼 수 있다.
- 로그인한 사용자는 댓글을 추가할 수 있다.
- 자신이 쓴 댓글에 한해 댓글을 삭제할 수 있다.
- Reply클래스는 내용과 어떤 게시글의 댓글인지를 저장한다.
- 작성자는 자신의 글을 삭제할 수 있다.
- 삭제하는 경우 데이터의 상태를 deleted로 바꾸고 화면에 출력하지 않는다.
- 댓글이 없는 경우 삭제가 가능하다
- 댓글이 있는 경우 모든 댓글이 자신의 댓글이면 삭제 가능하다.
- 게시글이 삭제되면 댓글도 삭제된다.
- AWS EC2를 이용하여 배포한다.

### 7단계
- ajax와 RestController를 이용하여 댓글 추가, 삭제를 구현한다.
- h2를 Mysql로 변경한다.
- 도커, 깃헙액션 등을 사용하지 않고 ec2에 배포한다.
- 댓글 추가를 성공한 경우 response에 OK status와 댓글 정보를 함께 보낸다.
- 추가 실패한 경우 Error status를 보낸다.
- ResponseEntity로 api 응답 반환

### 트러블슈팅
- spring data jpa를 연동하고 @Entity를 사용한 후 @ModelAttribute를 사용한 바인딩이 동작하지 않게 되었다.
  @Entity를 추가하며 기본 생상자를 추가했는데 스프링이 기본생성자를 우선해서 객체를 생성하고 이로 인해 기존에 작성해놨던 생성자가 동작하지 않아 발생한 문제였다. 
 setter를 추가해서 기본생성자를 이용하는 방법도 있겠지만 setter를 사용하기는 싫어서 UserDto를 만든 후 이를 User객체로 변환 후 db에 저장했다.

- jpa를 적용후 User테이블이 생기지 않는 문제가 발생했다. 원인은 h2가 user를 이미 예약어로 사용하고 있기 때문에 같은 이름의 테이블을 생성하지 못하는 것이었다.
 @Table의 name속성을 이용해 users로 테이블 이름을 바꾸니 잘 생성 되었다.

- 세션아이디가 쿠키에 중복되어 두개가 저장되는 문제가 있엇습니다. 쿠키에 세션아이디를 넣기 위해 HttpServletResponse를 사용했었습니다. 
 HttpServletResponse의 addCookie메소드를 이용홰야 세션아이디가 등록되는줄 알았는데 HttpSession을 매개변수로 사용만 하면 자동적으로 쿠키에 jsessionId가 등록된다는 것을 몰랐습니다.
 직접 세션아이디를 쿠키에 추가하는 로직을 제거해서 중복을 삭제했습니다.

- 컨트롤러의 로직을 서비스로 옮기면서 어디까지가 비즈니스로직일까에 대한 고민을 했습니다. 모델에 데이터를 넣는것도 서비스에서 처리해야 할지, 세션을 처리하는 것도 비즈니스 로직인지 고민을 했습니다.
 결과적으로 모델과 세션관련 로직은 컨트롤러에서 처리하게 했습니다. 모델이나 세션은 뷰와관련되어 있다보니 서비스에서 처리하면 SRP를 위반한다 생각했고 나중에 서비스를 테스트 할때도 어려움이 클 것이라 생각했습니다.

- 에러처리를 위해 서비스에서 에러를 던지고 컨트롤러에서 try-catch를 사용했습니다. 하지만 모든 메소드에서 try-catch를 사용하다보니 코드가 지저분해지는것 같아서 에러핸들러나 ControllerAdvice를 사용하는 것을 고민했습니다.

- 더미 데이터를 서버 실행시 생성하기 위해 user와 article을 생성하였습니다. 이때 @PostConstructor를 이용하여 객체를 생성하였고 Article의 경우 User가 생성되어 있어야 
 생성이 가능하기 때문에 객체 생성의 순서를 지킬 필요가 있었습니다. @DependsOn을 사용하여 User가 생성된 후 Article이 생성되도록 순서를 보장했습니다.

- 작성자가 아닌 유저가 url에 edit페이지를 입력하는 경우 에러 처리가 안되고 TemplateProcessingException이 발생했습니다. 처음에는 @ControllerAdvice를 이용해
 해당에러가 발생하는 것을 전역적으로 잡으려 했습니다. 그러나 컨트롤러에서 에러가 잡히지 않았고 뷰에서 템플릿을 렌더링 하는 동안 발생하는 에러라는 사실을 알게 되었습니다.
 해당 에러가 작성자를 확인 하는 로직 때문에 발생하고 글의 작성자가 아닌 경우 모델에 author를 담아주지 않았기에 템플릿에서 오류가 발생하게 되었습니다.
 이를 해결하기 위해 접근 권한이 없는 유저인 경우 AccessDeniedException을 발생시켜 에러가 뷰로 이어지지 않도록 처리 했습니다.

- matchArticleAuthor메서드를 컨트롤러에서 처리할지 서비스에서 처리할지 고민이 있었습니다. 예전 생각은 세션과 관련된 기능은 컨트롤러에서 처리하는게 더 명확하다는 생각이었는데 
 해당 메소드는 글의 작성자를 확인하고 작성자가 아니면 에러를 반환하다 보니 서비스 계층에서 일관되게 에러를 던지고 컨트롤러에서 처리하는 것이 더 좋은 구조가 아닌가 고민이 들었습니다.
 결론적으로 서비스에서 처리하기로 했습니다. 세션의 사용 여부로는 컨트롤러와 서비스의 책임을 구분할 수 없다고 생각했기때문에 비즈니스 로직이라는 책임에 더 걸맞는 서비스로 이동했습니다. 

- 어떤 경우에 예외를 써야할지 고민이 되었습니다. 이전까지는 DB에 값이 없거나 성공이 아닌 경우 전부 에러를 발생시켜왔는데 로그인을 실패하는 경우도 정상 로직의 일부라는 생각을 하니
 어떤 경우가 예외적인 상황인지 고민이 되었습니다. 결과적으로 예외를 써야한다고 판단한 경우는 DB같은 외부 시스템에 문제가 생긴경우, 입력이 정상적인 포맷이 아닌 경우, 비즈니스 로직 상 불가능한 경우를  
 예외처리 하기로 생각했습니다. 그래서 아이디 비밀번호를 질못 입력한 경우는 정상 로직으로 봤지만 게시글 상세 페이지를 볼 때 존재하지 않는 게시글 아이디를 요청한 경우, 
 글 작성시 작성자가 없는 경우 등 일반적이지 않은 요청은 예외 처리 하는 방식을 사용했습니다.

- 댓글을 삭제하는 경우 replyId에 해당하는 값을 찾지 못한 경우를 에러처리할지 일반 로직으로 볼지 고민이 들었습니다. 결과적으로 에러로 처리하였는데 그 이유는 
 댓글을 삭제 하는경우는 댓글에서 자신의 삭제 버튼을 누르는 방법 뿐인데 해당 버튼은 자신이 작성한 댓글에만 보이는 버튼이기 때문에 다른 유저가 작성한 댓글을 지우는 것은 정상적인 로직이 아니라고 생각이 들었습니다.

- 게시글 삭제를 구현하며 Article에 Reply와의 OneToMany관계를 만들지 고민했습니다. 기능상 단방향으로 구현하고 ArticleService에서 ReplyService를 의존성으로 주입해 처리하는 것도 가능했지만,
 ArticleService와 ReplyService사이에 의존성을 추가해야했습니다. 양방향을 선택하면 불필요한 조인이 발생하는 것을 우려했지만 실제 쿼리에선 isDeleted값을 변경하는 것처럼 단순한 필드값 변경에선 
 조인을 발생시키지 않고 엔티티별로 update를 진행했습니다. 

- 통합 테스트를 진행하며 서비스 클래스에서 만들었던 더미데이터들이 문제가 되었습니다. 의도는 테스트 클래스에서 만든 데이터들만을 가지고 테스트를 하는 것이었는데 @SpringBootTest로 인해
 스프링이 실행되면서 모든 더미데이터를 함꼐 생성했고 테스트 로직에 영향을 주게 되었습니다. 이를 해결하기 위해서 @Profile을 이용해 테스트 환경에서는 초기화 메소드가 실행 되지 않도록 해봤지만 메소드에만 
 적용이 되는 것이 아닌 클래스 전체에 적용되어 실패했습니다. 그 다음으로 Environment::getActiveProfiles로 test환경인 경우 초기화 함수가 바로 리턴되게 했습니다. 
 이 방법은 IDE에선 잘 실행이 됐지만 ./gradlew clean test를 한 경우는 적용되지 않았습니다. 최종적으로는 더미데이터를 삭제를 하기로 했습니다. 실제 웹페이지에서 테스트를 할 일이 크게 없기도 하고 
 테스트 메소드를 이용하는 것이 더 효율적이라 생각했습니다.

- 테스트를 작성하며 MissingMethodInvocationException이 발생했습니다. 원인은 MockHttpSession을 given()의 인자로 사용한 것이었습니다. given에는 Mockito의 Mock객체만 들어가야하는데 
 MockHttpSession은 Spring test에서 제공하는 HttpSession의 테스트용 구현체이기 때문에 해당 오류가 발생했습니다.

- 컨트롤러 테스트를 작성하면서 비슷한 로직을 어느정도로 테스트 해야할지가 고민되었습니다. 게시글을 수정하거나 삭제할때 DB에 해당 게시글이 있는지 확인하고 게시글의 작성자를 확인하는 로직이
 많이 중복되다 보니 테스트 로직도 중복되고 각 api의 반환값만 다른 정도였기에 내부 로직이 같다면 테스트를 하지않아도 괜찮은 것인지 아니면 인터셉터나 필터 등으로 중복 로직을 분리 한 후 한번만 
 테스트를 작성하는 것이 좋을지 고민이 되었습니다.

- 댓글 삭제를 ajax로 구현하면서 새로 생성된 댓글은 삭제 버튼이 보이지만 새로고침을 하면 버튼이 안보이는 문제가 있었습니다. 원인은 새로고침을 하면 기존 댓글 UI가 다시 서버에서 렌더링되는데,
  새로운 댓글을 추가할 때 updateRepliesUI() 함수에서만 삭제 버튼이 생성되었기 때문입니다. 서버에서 내려주는 템플릿에도 삭제 버튼을 추가하여 해결했습니다.