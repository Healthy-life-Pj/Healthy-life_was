# Healthy Life - Backend (WAS)

건강식품 전자상거래 플랫폼의 Spring Boot 백엔드 서버입니다.

## 기술 스택

| 분류 | 기술 |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2.9 |
| Build | Gradle |
| DB | MySQL |
| ORM | Spring Data JPA |
| Security | Spring Security + JWT |
| OAuth2 | Spring OAuth2 Client |
| Mail | Spring Mail |
| HTTP Client | Spring WebFlux (WebClient) |
| Crawling | Jsoup 1.17.2 |
| Validation | Jakarta Validation, Commons Validator |

## 프로젝트 구조

```
src/main/java/com/project/healthy_life_was/healthy_life/
├── client/          # 외부 API 클라이언트 (IamPort 등)
├── common/          # 공통 상수, 유틸리티
│   └── constant/    # API 경로, 응답 메시지 상수
├── config/          # 설정 (Security, CORS, Mail, WebClient)
├── controller/      # REST 컨트롤러
├── convertor/       # 엔티티 ↔ DTO 변환
├── dto/             # 요청/응답 DTO
├── entity/          # JPA 엔티티
├── handler/         # OAuth2 핸들러
├── repository/      # JPA 리포지토리
├── security/        # JWT 필터, PrincipalUser
└── service/         # 비즈니스 로직
    └── implement/   # 서비스 구현체
```

## API 엔드포인트

| 도메인 | Base Path |
|---|---|
| 인증 (Auth) | `/api/v1/auth` |
| 회원 (User) | `/api/v1/users` |
| 상품 (Product) | `/api/v1/products` |
| 주문 (Order) | `/api/v1/orders` |
| 결제 (Payment) | `/api/v1/payment` |
| 배송 (Shipping) | `/api/v1/shipping` |
| 위시리스트 | `/api/v1/wish-lists` |
| 장바구니 (Cart) | `/api/v1/carts` |
| 체형 (Physique) | `/api/v1/physiques` |
| 리뷰 (Review) | `/api/v1/reviews` |
| Q&A | `/api/v1/qnas` |
| 메일 (Mail) | `/api/v1/mail` |
| 배송지 | `/api/v1/deliver-addresses` |

## 주요 기능

- **회원 관리**: 회원가입, 로그인, OAuth2 소셜 로그인, 아이디/비밀번호 찾기
- **상품**: 상품 목록 조회, 상세 조회, 관리자 크롤링 등록 (Jsoup)
- **장바구니 / 위시리스트**: 상품 추가/삭제/수량 변경
- **주문 / 결제**: 직접 주문, 장바구니 주문, KG/IamPort 결제 연동 및 취소
- **배송**: 배송 상태 관리 (ShippingStatus Enum)
- **리뷰 / Q&A**: 주문 기반 리뷰 작성, Q&A 등록 및 답변
- **체형 태그**: 사용자 신체 정보 기반 맞춤 상품 추천 태그
- **이메일**: 인증 메일 발송 (Spring Mail)

## 인증 방식

JWT Bearer 토큰 기반 인증을 사용합니다.

```
Authorization: Bearer {token}
```

- 로그인 성공 시 JWT 토큰 발급
- Spring Security 필터 체인에서 토큰 검증
- OAuth2 로그인 성공 시 `OAuth2SuccessHandler`에서 토큰 발급

## 실행 방법

### 사전 요구사항
- Java 17
- MySQL 실행 중
- `application.properties` 또는 `application.yml` 환경 변수 설정 필요

### 빌드 및 실행

```bash
# 빌드
./gradlew build

# 실행
./gradlew bootRun

# 또는 jar 직접 실행
java -jar build/libs/healthy_life_was-0.0.1-SNAPSHOT.jar
```

### 기본 포트

```
http://localhost:4040
```

## 환경 변수 설정 예시 (`application.properties`)

```properties
# DB
spring.datasource.url=jdbc:mysql://localhost:3306/healthy_life
spring.datasource.username=root
spring.datasource.password=yourpassword

# JWT
jwt.secret=your_jwt_secret_key

# OAuth2
spring.security.oauth2.client.registration.google.client-id=...
spring.security.oauth2.client.registration.google.client-secret=...

# Mail
spring.mail.host=smtp.gmail.com
spring.mail.username=your@gmail.com
spring.mail.password=your_app_password

# IamPort
iamport.api-key=...
iamport.api-secret=...
```

## 연관 프로젝트

- **Frontend**: [Healthy-life_web](../Healthy-life_web) — React + TypeScript
