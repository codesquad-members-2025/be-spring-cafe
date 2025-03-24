# be-spring-cafe
2025 마스터즈 백엔드 스프링 카페
## 스프링 카페 - 회원 가입 및 목록 조회 기능
김영한님 기초 강의를 보면서 많이 따라했다.  
User 클래스를 만들었고, UserRepository인터페이스와 구현체를 만들어서 회원 추가 및 목록 조회 기능을 구현했다.  
UserController는 UserService를 통해 회원을 추가하거나 목록을 조회할 수 있도록 했다.  
중복되는 코드는 partial로 모았고 모든 static html들을 templates로 옮겨 동적으로 관리하도록 했다.  

## 스프링 카페 - 글 쓰기 기능 구현
게시글도 이제 도메인에 추가하여 관리하도록 함.  
회원정보 수정 기능도 추가하고, Put 메서드도 사용해봄.  

## 스프링 카페 - DB에 저장하기
H2 데이터 베이스를 추가하고 스프링 데이터 JPA를 사용해서 연결.  
추가로 에러가 발생하면 에러 메시지를 출력하도록 html과 컨트롤러 수정.  
배포 url - https://port-0-be-spring-cafe-m8ic5llb2a1ee439.sel4.cloudtype.app/
서버가 주기적으로 꺼져서 안될수도 있음!
