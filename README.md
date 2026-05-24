# 대출금 상환 프로젝트

## 1. 프로젝트 개요

[![Blog](https://img.shields.io/badge/Blog-프로젝트%20개요-blue?style=for-the-badge&logo=tistory&logoColor=white)](https://loan-project.tistory.com/19)

코어 뱅킹 개발자를 목표로 KB IT's Your life (개발 부트캠프) 에 참여하면서, 배운 기술과 은행 도메인 지식을 연결해보고 싶다는 생각이 들었습니다.

그래서 스터디를 꾸려 실제 KB 국민은행 가계 대출 상품 설명서에서 대출금 상환을 공부하고 이를 프로젝트로 구현 했습니다.

대출 신청 → 심사 → 승인 → 실행 → 대출금 상환의 전체 대출 프로세스 중 프로젝트의 범위는 **대출금 상환** 단계입니다.

대출 실행이 완료되어 고객에게 대출금이 입금된 상태를 전제로 합니다.

**구현 프로그램**은 세 가지입니다.

- **여신 담당자 업무 툴** — 은행 담당자가 고객의 대출 및 상환 현황을 조회·관리하는 내부 프로그램
- **고객 상환 서비스** — 고객이 직접 상환을 요청하고 상환 내역을 확인하는 서비스 프로그램
- **배치** — 매일 정해진 시간에 이자 계산, 상환 및 계좌 상태 관리 등을 수행하는 프로그램

---

## 2. 가계 대출 상품 설명서 분석

[![Blog](https://img.shields.io/badge/Blog-상품%20설명서%20분석%20①-blue?style=for-the-badge&logo=tistory&logoColor=white)](https://loan-project.tistory.com/25)
[![Blog](https://img.shields.io/badge/Blog-상품%20설명서%20분석%20②-blue?style=for-the-badge&logo=tistory&logoColor=white)](https://loan-project.tistory.com/26)
[![PDF](https://img.shields.io/badge/PDF-가계대출%20상품%20설명서-red?style=for-the-badge&logo=adobeacrobatreader&logoColor=white)](./docs/가계대출상품설명서.pdf)

실제 KB 국민은행 **가계대출 상품 설명서**를 개발자 시각으로 분석했습니다.

- **대출금리** — 고정 / 변동 / 혼합 금리 유형별 계산 방식 및 기준금리 변동 이력 관리
- **상환방법** — 원리금 균등 / 원금 균등 / 만기 일시상환, 금리 유형과의 조합(6가지) 스케줄 계산
- **중도상환 수수료** — 수수료 계산 공식 및 면제 조건(3년 경과 / 만기 90일 이내 / 대환 합산 3년 초과) 분기 처리
- **대출계약 철회권** — 철회 가능 기간 계산, 반환 금액 산정, 남용 방지 로직
- **연체 이자** — 연체이자율 산정, 상환 방법·연속 연체 횟수·경과 기간에 따른 연체금 분기 처리
- **채무변제 충당순서** — 비용 → 이자 → 원금 순 차감, 기한이익 상실 시 충당 순서 변경 처리
- **금리 인하 요구권** — 신청 가능 여부 판단, 10영업일 처리 기한 관리, 신청 상태 흐름 관리

---

## 3. 요구사항 정리

![WIP](https://img.shields.io/badge/Blog-작성%20예정-lightgrey?style=for-the-badge&logo=tistory&logoColor=white)

분석한 상품 설명서를 바탕으로 요구사항을 정리했습니다.

---

## 4. ERD 설계

![WIP](https://img.shields.io/badge/Blog-작성%20예정-lightgrey?style=for-the-badge&logo=tistory&logoColor=white)

도출된 요구사항을 기반으로 테이블 구조를 설계했습니다.

---

## 5. 프로그램 설계

![WIP](https://img.shields.io/badge/Blog-작성%20예정-lightgrey?style=for-the-badge&logo=tistory&logoColor=white)

ERD를 바탕으로 실제 구현에 앞서 프로그램의 구조와 흐름을 설계했습니다.

---

## 6. 구현 및 테스트

![WIP](https://img.shields.io/badge/Blog-작성%20예정-lightgrey?style=for-the-badge&logo=tistory&logoColor=white)

설계한 내용을 코드로 구현하고, 각 기능에 대한 테스트를 진행했습니다.

---

## 7. 유지보수

![WIP](https://img.shields.io/badge/Blog-작성%20예정-lightgrey?style=for-the-badge&logo=tistory&logoColor=white)

구현 이후 발생한 문제 및 개선 사항을 정리했습니다.
