# Withins Mail

## 의존성

```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-mail'  // 핵심 의존성
    implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
    implementation project(':core')
}
```

## 설정
```yaml
spring:
  mail:
    host: [SMTP 서버 주소]
    port: [SMTP 포트 번호]
    username: [사용자 이메일]
    password: [사용자 앱 비밀번호]
    properties:
      mail:
        smtp:
          auth: true
          timeout: 5000
          starttls:
            enable: true
          ssl:
            trust: smtp.gmail.com  # 추가

```

## 모듈 구조

```markdown
src/main/java/com/withins/mail/
├── 📁 component/
│   └── 📄 MailSendExceptionHandlerHelper       # 메일 전송 예외 템플릿
├── 📁 config/
│   ├── 📄 MailConfig                           # 메일 설정 클래스
│   └── 📄 MailConfigurationProperties          # 설정 프로퍼티
├── 📁 dto/
│   └── 📄 ⭐ MailSendDto                       # 메일 전송 요청 DTO
├── 📁 enums/
│   └── 📄 EmailTemplate                        # HTML 메일 템플릿 상수
├── 📁 exceptions/
│   ├── 📄 EmailExternalException               # 400 BadRequest : 사용자에 의한 예외
│   │   ├── 📄 InvalidEmailException            # 잘못된 이메일 형식 예외
│   │   └── 📄 InvalidEmailException            # 잘못된 이메일 형식 예외
│   └── 📄 EmailInternalException               # 500 InternalServerError : 서버 내부 설정에 의한 예외
│       ├── 📄 EmailConnectionException         # SMTP 잘못된 주소 또는 포트 예외
│       ├── 📄 SMTPBadCredentialsException      # SMTP 잘못된 아이디 또는 비밀번호 예외
│       └── 📄 MailTemplateNotLoadedException   # HTML 템플릿 예외
├── 📁 service/
│   ├── 📄 ⭐ MailSender                        # 메일 전송 서비스 인터페이스 
│   └── 📄 MailServiceImpl                      # 메일 전송 서비스 구현체
├── 📁 repository/
│   ├── 📄 MailRepository                       # 메일 전송 서비스 인터페이스
│   └── 📄 MailRepositoryImpl                   # 메일 전송 서비스 구현체
└── 📁 resources/
    ├── 📁 template/                            # 메일 전송 서비스 인터페이스
    │   └── 📄 emailAuth.html                   # 메일 인증 템플릿
    └── 📄 mail.yml                             # 설정파일
```


## 주요 클래스

- [`MailSender`](src/main/java/com/withins/mail/service/MailSender.java) : 메일 전송 서비스 인터페이스
- [`MailSendDto`](src/main/java/com/withins/mail/dto/MailSendDto.java) : 메일 전송 요청 DTO


## 사용방법

### 이메일 인증 전송
```java
@Controller
@RequiredArgsConstructor
public class MailTestController {

    private final MailSender mailSender;

    @GetMapping("/test/mail")
    public ResponseEntity<String> sendAuth() {
        mailSender.sendAuth("withins.help@gmail.com", 12345);
        return ResponseEntity.ok("OK");
    }
    
}
```

### Text, HTML 이메일 전송
```java
    @GetMapping("/test/mail")
    public ResponseEntity<String> sendAuth() {
        MailSendDto sendDto = MailSendDto.to("asdf@gmail.com").write("제목", "내용");
        mailSender.sendHtml(sendDto);
        mailSender.sendText(sendDto);
        return ResponseEntity.ok("OK");
    }
```
Text, HTML 은 같은 객체를 사용합니다. Text 는 SimpleMailMessage 객체를 사용하고 HTML 은 MimeMessage 객체를 사용하기 때문에
이메일 내용에 html 요소가 들어갔다고해도 sendText로 보내면 문자열 그대로 전송됩니다.

### MailSendDto 사용법

```java
MailSendDto mail = MailSendDto
    .to("[이메일]")
    .write("[제목]", "[내용]");
```
MailSendDto 은 내부 빌더에 의해 만들어집니다 따라서 이메일, 제목, 내용이 모두 작성되어야 객체를 생성할 수 있습니다.
```java
to(String... addresses);
write(String subject, String content);
```

보낼 메일은 가변인자를 지원합니다.



## 테스트 코드
- [`MailApplicationTests`](src/test/java/com/withins/mail/MailApplicationTests.java) : 이메일에 전송될 HTML 양식에 대한 검증


## HTML 템플릿 추가
- [`EmailTemplate`](src/main/java/com/withins/mail/enums/EmailTemplate.java)

1. HTML 템플릿을 resources/template 에 추가 [`바로가기`](src/main/resources/template)
2. [`EmailTemplate`](src/main/java/com/withins/mail/enums/EmailTemplate.java) 에 상수 등록
3. **선택사항** HTML 변수는 {{%s}} 으로 작명 중괄호와 변수명 사이 띄어쓰기 불가

