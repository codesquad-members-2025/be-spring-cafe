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