# 🛠️ Spring 심화 프로젝트

## 📌 Level 0. 프로젝트 초기 설정
- **application.yml 없음**
    - 오류: `Could not resolve placeholder 'jwt.secret.key'`
    - 해결: `src/main/resources/application.yml` 생성 후 jwt.secret.key 추가
- **DB 설정 누락**
    - 오류: `Failed to configure a DataSource: 'url' attribute is not specified...`
    - 해결: DB 연결 설정 추가 (url, username, password 등)

---

## 📌 Level 1. Argument Resolver 오류
- 문제: `AuthUserArgumentResolver` 동작 안 함
- 원인: MVC 설정 미등록
- 해결: `WebConfig`에 `addArgumentResolvers` 등록

---

## 📌 Level 2. 코드 리팩토링
- **Early Return** → 이메일 중복 시 비밀번호 인코딩 전에 리턴
- **불필요한 if-else 제거** → 조건 분기 단순화
- **Validation 개선** → DTO에 `@NotBlank`, `@Size`, `@Pattern` 적용

---

## 📌 Level 3. N+1 문제
- 문제: Todo 조회 시 User를 N+1 쿼리로 가져옴
- 해결: `@EntityGraph(attributePaths = {"user"})` 적용

---

## 📌 Level 4. 테스트 코드 수정
- `passwordEncoder.matches(raw, encoded)` 순서 수정
- 테스트 메서드 이름/에러 메시지 개선
- `todo.user == null` 케이스 서비스 로직 처리

---

## 📌 Level 5. Admin API 로깅 (AOP)
- `@Around` AOP로 요청/응답 로깅
- Request/Response 정보, 상태코드, 직렬화(JSON) 처리

---

## 📌 Level 6. 개선 방향
- 매직 스트링 → 상수화 (`"ADMIN"`, `"USER"`)
- 에러 메시지 → `ErrorCode Enum` 통합 관리
- 공통 에러 응답 → `ErrorResponseDto` 적용
- `findByIdOrElseThrow` → 정적 팩토리 메서드 패턴

---

## 📌 Level 7. 테스트 코드 작성
- 서비스/리포지토리 단위 테스트
- Validation 및 예외 케이스 테스트
<img width="500" height="270" alt="image" src="https://github.com/user-attachments/assets/32c68de3-6021-4473-90d0-bd7b3d0415a9" />

---

## 🐞 트러블슈팅 사례
1. **IntelliJ 테스트 실패** → 경로에 한글 포함 → 영어 경로로 변경
2. **AOP 로깅 DTO 직렬화 문제** → `ObjectMapper.writeValueAsString()` 적용
3. **매직 스트링 중복** → 상수로 치환
4. **커스텀 예외 처리 개선** → 도메인별 에러코드 반환 가능
5. **에러 코드 설계** → `ErrorCode Enum` 통합 관리
6. **default method Mock 문제** → `CALLS_REAL_METHODS` 또는 `thenThrow` 활용

---
