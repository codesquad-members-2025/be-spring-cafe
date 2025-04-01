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
배포는 CloudType으로 했다.  
클라우드 타입은 DOCKER를 쓰지 않고도 시작 명령어와 포트 주소만 전달해주면 배포가 가능했다.  
처음으로 배포해보는 거라 가장 쉬운 방법으로 접근했는데, 나중에 DOCKER나 리눅스 명령어에 대해 알아봐야겠다고 생각함.

## 스프링 카페 - 로그인 구현
HttpSession을 이용해서 로그인을 구현했다.  
로그인 시에 세션에 유저를 추가하고, ControllerAdvice에서 전역적으로 로그인을 확인했다.  
ControllerAdvice를 쓰니 MvcConfig로 ViewController에 전달을 할때, 로그인이 풀렸다.  
그 이유는 정확히 모르지만 아마 ControllerAdvice에서 처리하기 전에 MvcConfig에서 먼저 처리를 해버려서인 것 같은데, 나중에 더 알아봐야겠다.  
예외상황(로그인 실패나 다른 유저의 권한에 접근 등)을 null로 처리해야하나, exception으로 처리해야하나 고민을 했는데, ChatGPT의 조언대로 exception으로 처리했다.  
ChatGPT는 Exception을 추천했는데, 아무래도 전역적으로 처리할수도 있고, 예외상황 발생시 사용자에게 어떤 예외상황이 발생했는지 알려주기 좋다는 점에서 그렇게 추천했다고 한다.  
그렇게 유저가 로그인을 해야만 자신의 정보를 수정할 수 있도록 하는 기능도 구현을 완료했다.  

## 스프링 카페 - 게시글 권한부여
로그인 여부와 해당 게시물의 사용자인지의 여부에 따라 게시물 조회와 수정 및 삭제가 가능하도록 했다.  
로그인 여부는 원래 컨트롤러 내부에서 처리했는데, 수정하여 인터셉터를 사용하도록 했다.  
인터셉터를 사용하여 로그인을 확인하면 좋은 점은, 반복되는 로그인 확인 절차를 여러 컨트롤러에서 중복해서 사용할 필요가 없다는 것이다.  
또한, if문 로직에서 정상 상황을 잡을지, 예외상황을 잡을지 고민했는데, 예외를 먼저 처리하고, 정상 로직을 아래에 두는 Guard Clause 스타일을 사용했다.  
이 패턴에선 비정상 상황은 빨리 return/throw 해버리고 정상 흐름은 아래로 배치하여 가독성을 높인다.  

## 스프링 카페 6단계 - 댓글
댓글을 구현하고, 게시글과 댓글에 소프트 딜리트를 구현했다.   
where을 엔티티에 달아, delete = false인 엔티티들만 사용하도록 했다.  
또한 게시글과 댓글을 양방향으로 관계를 맺도록하여, 게시글에서 댓글을 편하게 갖고오도록 했다.  
EC2로 배포하니 게시글과 댓글 작성 시간이 현재시간과 차이가 나는 문제가 생겼다.  
이는 EC2의 시간은 서울 시간을 쓰는 것이 아니라 협정 세계시를 사용하기 때문에, 코드에서 명시적으로 서울 시간을 쓰도록 수정했다.  
도커를 사용해서 배포를 했고, 편리한 배포를 위해 deploy.sh파일도 만들었다.  

