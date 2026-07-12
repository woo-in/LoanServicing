# CoreBanking

- 대출 서비스 플랫폼의 코어뱅킹 시스템
- 주 개발 요소는
1. 상환하기 기능 API 요청에 대한 응답 
2. 일 배치 프로그램
3. API 와 배치에서 사용하는 컴포넌트   


## 기술 스택

- Java 17
- Spring Boot 4.1.0
- MyBatis 3.0.4 (Spring Boot 4.x 자동구성 미지원으로 `MyBatisConfig`에서 수동 구성)
- MySQL / HikariCP
- Spring Security Crypto (BCrypt 비밀번호 암호화)
- Spring Batch
- Lombok

## 데이터베이스

- DB명: `loan_servicing`
- Host: `localhost:3306`
- Connection Pool: HikariCP (max 10 / min idle 5)

## 패키지 구조

```
hello.corebanking
├── controller/          # API 진입점
├── batch/               # 배치 (job, step, reader, writer, listener)
├── domain/
│   ├── customer/        # 회원, 주계좌
│   ├── product/         # 이자종류, 상환방법, 대출상품
│   ├── loan/            # 대출계약, 대출계좌, 대출상태, 대출상태이력
│   └── repayment/       # 상환스케줄, 상환이력, 연체상태, 연체이력
└── global/
    ├── config/          # MyBatisConfig, PasswordEncoderConfig
    ├── exception/       # 공통 예외 클래스
    └── util/            # AccountNumberGenerator 등 공통 유틸
```

### 도메인 패키지 구조 패턴
모든 도메인은 아래 구조를 따른다.
```
domain/{도메인명}/
├── dto/          # 요청/응답 DTO
├── entity/       # 도메인 엔티티 (순수 POJO, Lombok @Getter @NoArgsConstructor)
├── repository/   # MyBatis @Mapper 인터페이스
└── service/
    ├── component/  # @Component Validator 등
    └── *Service.java
```

### MyBatis
- Mapper 인터페이스에 `@Mapper` 어노테이션 사용
- SQL은 `src/main/resources/mapper/**/*.xml`에 작성
- `camelCase ↔ snake_case` 자동 매핑 (`MyBatisConfig`에서 설정)
- 조회 시 `Optional` 반환, 없으면 `NotFoundException`

### 예외
- `global/exception/`에 공통 예외 정의
- 모두 `RuntimeException` 상속
- 현재 정의된 예외: `NotFoundException`, `DuplicateLoginIdException`, `PasswordMismatchException`, `DuplicateAccountException`


## Git 컨벤션

### 브랜치 전략
- `main`: 배포 브랜치
- 작업 브랜치: `feat/`, `fix/`, `chore/`, `docs/` 접두사 사용

### 커밋 메시지
```
type: 한국어로 작성된 설명
```
예) `feat: 회원가입 entity/repository/service 구현`

## GitHub 작업 플로우 (이슈 → PR 자동화)

새 작업을 시작할 때는 아래 순서를 **사용자 확인 없이 연속으로** 진행한다 (`gh` CLI 사용, 이미 인증되어 있음). 이슈 등록부터 PR 생성·마일스톤 연결까지는 사전 승인된 자동화 범위다.

1. **이슈 등록** (`gh issue create`)
   - 제목: `[TYPE] 한국어 설명` (`.github/ISSUE_TEMPLATE/*.md` 참고)
   - 라벨은 제목 접두사와 반드시 1:1로 매칭:

     | 접두사 | 라벨 |
     |---|---|
     | `[BUG]` | `버그` |
     | `[DOCS]` | `문서` |
     | `[FEAT]` | `기능` |
     | `[LEARN]` | `학습` |
     | `[TASK]` | `작업` |
     | `[QUESTION]` | `질문` |
     | `[REFACTOR]` | `리펙토링` |
   - 본문은 접두사에 대응하는 `.github/ISSUE_TEMPLATE/*.md`의 섹션 구조를 그대로 따른다 (템플릿마다 섹션 구성이 다르므로 아래 표대로 매칭):

     | 접두사 | 템플릿 파일 | 섹션 구성 |
     |---|---|---|
     | `[BUG]` | `bug_report.md` | 버그 설명 / 재현 방법 / 예상 동작 / 실제 동작 / 스크린샷 / 환경 / 추가 정보 |
     | `[FEAT]` | `feature_request.md` | 기능 요약 / 배경 및 이유 / 구현 방법 나열 / 추가 정보 |
     | `[DOCS]` | `docs.md` | 문서 작업 내용 / 작업 이유 / 작업 범위(체크박스) / 참고 자료 |
     | `[LEARN]` | `learn.md` | 무엇을 했나 / 왜 이렇게 했나 / 어떻게 동작하나 / 몰랐다가 알게 된 것 / 참고 사항 |
     | `[TASK]` | `task.md` | 작업 내용 / 작업 목표 / 세부 작업 목록(체크박스) / 참고 자료 / 완료 조건 |
     | `[QUESTION]` | `question.md` | 질문 내용 / 배경 / 시도해본 것 / 참고 자료(+코드블록) |
     | `[REFACTOR]` | `refactor.md` | 리팩토링 대상 / 현재 문제점 / 개선 방향 / 예상 영향 범위(체크박스) / 주의사항 |
   - 관련 마일스톤이 있으면 `--milestone`으로 바로 연결
2. **브랜치 생성**: `main` 최신화(`git pull origin main`) 후 위 "브랜치 네이밍 규칙"(`{type}/{설명}`)대로 분기
3. **구현 + 테스트**: 기존 컴포넌트/테스트 컨벤션을 따르고 `./gradlew test` 통과 확인 후 다음 단계로
4. **커밋**: `type: 한국어 설명` 형식 + `Co-Authored-By: Claude Sonnet 5 <noreply@anthropic.com>` 트레일러. 관련 없는 미추적 파일은 같이 add하지 않는다
5. **push**: `git push -u origin {브랜치명}`
6. **PR 생성** (`gh pr create`)
   - 제목: `[#이슈번호] type: 작업 내용` (`.github/PULL_REQUEST_TEMPLATE.md` 형식)
   - 본문: 관련 이슈(`closes #N`) / 작업 내용 / 변경 유형(체크박스, 커밋 type과 동일한 것 체크) / 체크리스트 / 리뷰어에게 전달할 내용
   - 이슈가 마일스톤에 연결돼 있으면 PR도 `--milestone`으로 같은 마일스톤에 연결(진행률에 이슈+PR이 함께 집계되도록)

이슈가 여러 개로 쪼개지는 큰 작업(예: 컴포넌트 체인)은 먼저 마일스톤을 만들고 하위 이슈들을 거기 묶은 뒤 이 플로우를 반복한다.

## 테스트

- 서비스 레이어: Mockito 단위 테스트 (`@ExtendWith(MockitoExtension.class)`)
- Mapper는 Mock으로 대체하여 DB 없이 실행 가능
- 테스트 실행: `./gradlew test`
- 빌드: `./gradlew build`

## 주요 설정 파일

- `global/config/MyBatisConfig.java`: SqlSessionFactory 수동 구성 (Spring Boot 4.x 호환 이슈)
- `global/config/PasswordEncoderConfig.java`: BCryptPasswordEncoder 빈 등록
- `CoreBankingApplication.java`: `@MapperScan` 으로 Mapper 수동 스캔
