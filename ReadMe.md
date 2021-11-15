### 회원가입 및 로그인 서비스

#### API 사용법
1. Springboot 프로젝트를 빌드 후 인텔리제이 등을 이용하여 jar 를 실행합니다.
2. 8088 포트로 접속이 가능 합니다. (http://localhost:8088/swagger-ui.html)
3. Swagger 문서에 각 URI 에 대한 설명을 참고하여 쿠폰다운로드 테스트 진행이 가능 합니다.

#### 어플리케이션 구성
- MemberController
  - 회원가입 및 회원로그인, 회원조회 등의 기능을 수행합니다.
- SmsService, MemberService
  - 인증문자발송 및 발송문자확인을 수행하고 회원가입,로그인,조회 및 비밀번호 변경을 수행합니다.
  - 테스트가 원활하기 위해 어플리케이션 초기 구동 시 테스트 회원을 1명을 등록 합니다. (이메일 : acetious@gmail.com, 휴대폰 : 1111)
  - 또한 로그아웃을 하여 로그인이 되지 않은 상태에서 회원정보를 조회하지 못하도록 합니다.

#### 테스트 시나리오
1. http://localhost:8088/swagger-ui.html 에 접속합니다.
2. /member/send-join-token URL 로 가입에 필요한 인증문자 발송을 합니다.
3. http://localhost:8088/h2-console 로 접속 후에 select * from message 쿼리를 실행하여 발송된 message 를 확인합니다.
4. 해당 메시지를 클립보드에 복사합니다.
3. /member/phone-check-join-token URL 를 호출하며 이때 발송 메시지를 입력하여 회원 가입에 필요한 휴대폰번호 인증절차를 완료합니다.
4. /member/join  URL 로 회원가입절차 를 완료합니다.
5. /member/login 회원가입에서 입력하였던 회원 계정으로 로그인을 진행 합니다.
6. /member/reset-password URL 로 임시비밀번호를 요청을 진행 합니다.
7. http://localhost:8088/h2-console 에서 select * from mesage 쿼리를 실행하여 변경이 된 임시비밀번호 메시지를 확인합니다.
6. /member/login URL 로 변경이 된 임시비밀번호로 로그인을 진행 합니다.
7. /member/me-info URL 로 로그인 된 상태에서 나의정보 조회가 가능한지 확인 합니다.

#### 인메모리 H2 DB 정보
http://localhost:8088/h2-console/
- 계정 : sa / password