# be-spring-cafe

2025 마스터즈 백엔드 스프링 카페

---
## 배포 방법 정리
1. DB를 **파일 모드**로 변경 -> 서버 한 번 실행하면 data/demo-db.mv.db 파일 자동 생성됨!
   1. 웹 브라우저 콘솔 접속 가능
      2. http://localhost:8080/h2-console
   2. 데스크탑 GUI (h2.sh) 접속도 가능 -> 절대경로 사용
      3. jdbc:h2:file:/Users/gyuwon/your-project/data/springcafe-db
* H2 DB의 3 가지 모드
  * 메모리 모드
    * jdbc:h2:mem:testdb
    * 메모리에만 존재, 서버 꺼지면 데이터 사라짐
  * TCP 모드
    * jdbc:h2:tcp://localhost/~/test
    * 다른 프로세스에서도 접근 가능 (서버 모드)
    * 거의 사용 안함
  * 파일 모드
    * jdbc:h2:file:./data/demo-db 
    * 디스크에 .mv.db 파일로 저장됨.
    * 서버 꺼도 남아있음
    * 실제로 생기는 파일
    ```angular2html
    /your-project-folder
    └── data/
    └── demo-db.mv.db ← 이게 H2 데이터베이스 파일!
    ```
    
2. CloudType에 반영하기
    * Cloudtype은 기본적으로 .gitignore에 있는 건 무시 -> DB 파일이 있는 data/ 폴더를 포함해야 함.
    * .gitignore 수정
   ```angular2html
   // 이런 항목 주석 처리하거나 삭제
   - /data
   - *.mv.db
    ```
   
3. Git에 반영하고 Cloudtype에 배포
   ```angular2html
    git add .
    git commit -m "Set H2 to file mode for Cloudtype"
    git push
    ```
4. Cloudtype에서 동작 확인 
   1. 브라우저 열기 -> 주소: https://your-cloudtype-app-url/h2-console
   2. 설정:
   ```angular2html
    항목	값
    JDBC URL	jdbc:h2:file:./data/demo-db
    Username	sa
    Password	비워도 OK
    ✅ 접속 성공 → 기존에 저장된 데이터 확인 가능!
    ```
   
---
## Step 5-1 트러블슈팅
**컨트롤러와 뷰 컨트롤러의 충돌:**
* 뷰 컨트롤러는 기본적으로 get 요청만 처리 -> post 방식으로 "/user/login"에 로그인 폼을 제출하면, 이 뷰 컨트롤러가 먼저 매핑되어 post 요청을 처리하지 못해 405 오류 발생
* UserController에도 POST 방식으로 "/users/login"을 처리하는 메소드가 있음. 
* 그런데 MvcConfig에서는 setOrder(Ordered.HIGHEST_PRECEDENCE)를 설정해서 이 뷰 컨트롤러가 가장 높은 우선순위를 가지게 됨.
* 결과적으로, 클라이언트가 "/users/login"으로 POST 요청을 보내면, 스프링은 가장 높은 우선순위를 가진 뷰 컨트롤러를 선택하게 되고, 이 뷰 컨트롤러는 GET만 지원하므로 POST 요청을 처리하지 못함.
* 만약 setOrder(우선순위)를 제거하면 오류가 발생 안 할 가능성도 있지만, 모호한 매핑 구조를 남기게 되고, url이 준복 등록되어 혼란이 일어날 수 있기 때문에 user 컨트롤러에서 get과 post 방식으로처리.

**테스트 데이터를 미리 import 하는 방식으로 하고싶었는데 file 형식으로 db를 저장해서 인지 import.sql이 반영 안됨** 
  * 직접 추가해줌
  * H2 db에 직접 등록하는 방법도 있을 듯

**서버 측에서는 각 요청마다 세션 정보와 대상 id를 재검증하여, 본인이 아닌 다른 사용자의 정보를 수정하지 못하도록 해야 함**
  * GET 방식의 수정 폼을 호출할 때 이미 로그인 여부와 본인 여부를 확인했다 하더라도, 보안상의 이유로 최종 업데이트 요청(여기서는 PUT 요청)에서도 다시 한 번 검증하는 것이 좋음
  * HTTP는 무상태(Stateful하지 않음):
    * 각 요청은 독립적이므로, GET 요청에서 확인한 정보가 자동으로 후속 요청에 보장되지 않음.
    * 예를 들어, 누군가 수정 폼의 HTML을 조작해서 다른 사용자의 id를 포함한 PUT 요청을 보내면, 서버에서 다시 확인하지 않으면 보안 문제가 발생할 수 있음
  * profile, updateForm, updateUser 모두에서 로그인 여부와 세션 사용자와 수정 대상 id가 일치하는지 확인하는것이 좋음

**컨트롤러에서 로그아웃 post로 작성 -> html 폼에서는 get 방식** 
  * get 요청이 들어왔을 때 405오류 발생
  * 로그아웃 링크 대신 POST 요청을 보내도록 폼 사용해야함 
    * 로그아웃을 POST 방식으로 보내기 위해서는 html 폼 사용해야 함. -> 브라우저는 현재 세션 쿠키를 자동으로 포함하여 서버에 POST 요청
    * 
## Step 5-1 트러블슈팅
**엔티티 내부에서 update 메서드 정의 **
* 컨트롤러에서 setter 사용 안하기 위한 목적
    * 엔티티 내부에 update 메서드를 두면 서비스나 컨트롤러에서는 update DTO를 받아서 한 줄로 업데이트할 수 있다.
    * 컨트롤러 메서드 파라미터에 @ModelAttribute를 사용하여 자동으로 DTO 객체로 바인딩
      * updateDto를 직접 new 키워드로 생성할 필요 없음
      