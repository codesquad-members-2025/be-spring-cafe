# be-spring-cafe
2025 마스터즈 백엔드 스프링 카페
## 스프링 카페 - 회원 가입 및 목록 조회 기능
김영한님 기초 강의를 보면서 많이 따라했다.  
User 클래스를 만들었고, UserRepository인터페이스와 구현체를 만들어서 회원 추가 및 목록 조회 기능을 구현했다.  
UserController는 UserService를 통해 회원을 추가하거나 목록을 조회할 수 있도록 했다.  
중복되는 코드는 partial로 모았고 모든 static html들을 templates로 옮겨 동적으로 관리하도록 했다.  