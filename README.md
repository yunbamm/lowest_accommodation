# 최저가어때? (Lowest Accommodation)

> 야놀자 숙소 가격을 주기적으로 추적하여 최저가를 찾아주는 가격 모니터링 시스템

## 📌 프로젝트 개요

사용자가 관심있는 숙소를 등록하면, 두 가지 방식(실시간/스케줄러)으로 가격을 추적하여 최저가가 나타났을 때 알림을 제공하는 서비스입니다.

## 🎯 학습 목표

이 프로젝트를 통해 다음 기술 스택을 실전에서 학습합니다:
- ✅ **Spring Boot**: REST API, Scheduler, Event
- ✅ **JPA**: Entity 설계, 쿼리 최적화, 대량 데이터 처리
- ✅ **Docker**: 컨테이너화, Docker Compose, 멀티 스테이지 빌드
- ✅ **Redis**: 캐싱 전략, TTL 관리, Pub/Sub
- ✅ **Kafka**: Producer/Consumer, 작업 큐, 이벤트 스트리밍
- ✅ **AWS**: EC2, RDS, S3, 보안 그룹 설정
- ✅ **CI/CD**: GitHub Actions, 자동 배포 파이프라인

## 🎯 핵심 기능

### 1. 관심 숙소 등록
- 야놀자 숙소 URL, 체크인/체크아웃 날짜, 희망 가격 등록
- 중복 등록 방지 (같은 숙소 + 같은 날짜)

### 2. 실시간 가격 조회 (Foreground) 🔥
**사용자가 버튼 클릭 시 즉시 조회**

```
[사용자 요청]
    ↓
[Redis 캐시 확인] ← 캐시 히트 시 즉시 응답 (Redis ⭐)
    ↓ 캐시 미스
[Kafka: "crawling-tasks" 토픽 발행] (Kafka Producer ⭐)
    ↓
[Crawling Worker 비동기 처리] (Kafka Consumer ⭐)
    ↓
[Selenium 크롤링]
    ↓
[Redis 캐싱 (30분 TTL) + DB 저장] (Redis + JPA ⭐)
    ↓
[사용자에게 결과 응답]
```

**기술 학습 포인트:**
- Redis 캐싱 전략 (Cache-Aside Pattern)
- Kafka 비동기 작업 큐
- 크롤링 결과 실시간 응답 처리

---

### 3. 스케줄러 자동 조회 (Background) 🕐
**6시간마다 자동으로 모든 감시 숙소 가격 추적**

```
[Spring @Scheduled (6시간 주기)] (Spring Boot ⭐)
    ↓
[활성 감시 숙소 목록 조회] (JPA ⭐)
    ↓
[Kafka: "crawling-tasks" 토픽 대량 발행] (Kafka Producer ⭐)
    ↓
[Multiple Crawling Workers 병렬 처리] (Kafka Multi-Consumer ⭐)
    ↓
[Selenium 크롤링]
    ↓
[Kafka: "price-data" 토픽 발행] (Kafka ⭐)
    ├─→ [PriceHistory DB 저장] (JPA ⭐)
    └─→ [최저가 체크 Service]
            ↓ 최저가 발견!
        [Kafka: "notifications" 토픽 발행] (Kafka ⭐)
            ↓
        [Notification Worker]
            ├─→ FCM 푸시 알림
            └─→ SMS 문자 알림
```

**기술 학습 포인트:**
- Spring Scheduler 설정 및 관리
- Kafka 대량 작업 처리
- JPA 배치 처리 및 최적화
- 멀티 Consumer 패턴

---

### 4. 최저가 알림 시스템 🔔

```
[최저가 발견]
    ↓
[Kafka: "notifications" 토픽] (Kafka ⭐)
    ├─→ [FCM Worker] → 모바일 푸시
    ├─→ [SMS Worker] → 문자 알림
    └─→ [Email Worker] → 이메일 (선택)
```

**기술 학습 포인트:**
- Kafka 멀티 Consumer 그룹
- 외부 API 연동 (FCM, SMS)
- 알림 실패 시 재처리

---

## 🛠 기술 스택 상세

### Backend
| 기술 | 용도 | 학습 내용 |
|------|------|-----------|
| **Spring Boot 3.x** | Core Framework | REST API, Scheduler, Configuration |
| **JPA (Hibernate)** | ORM | Entity 설계, N+1 해결, 쿼리 최적화 |
| **PostgreSQL** | Main Database | 관계형 DB 설계, 인덱스 최적화 |
| **Redis** | Cache & Session | 캐싱 전략, TTL, Pub/Sub |
| **Kafka** | Message Queue | Producer/Consumer, 작업 큐, 이벤트 스트리밍 |
| **Selenium** | Web Crawling | 동적 페이지 크롤링 |

### Infrastructure
| 기술 | 용도 | 학습 내용 |
|------|------|-----------|
| **Docker** | Containerization | Dockerfile, Docker Compose, 멀티 스테이지 빌드 |
| **AWS EC2** | Server | 인스턴스 관리, 보안 그룹 |
| **AWS RDS** | Managed DB | PostgreSQL 관리형 서비스 |
| **GitHub Actions** | CI/CD | 자동 테스트, 빌드, 배포 |

### External APIs
~~- **Firebase FCM**: 모바일 푸시 알림~~
- **SMS API**: 문자 알림 (NCP SENS / Twilio)

---

## 📂 프로젝트 구조

```
src/main/java/bangbang/
├── controller/              # REST API endpoints
│   ├── MemberController
│   ├── WatchedAccommodationController
│   └── PriceController
├── service/                 # Business logic
│   ├── MemberService
│   ├── WatchedAccommodationService
│   ├── CrawlingService
│   ├── PriceService
│   ├── NotificationService
│   └── SchedulerService     # 스케줄러
├── kafka/                   # Kafka Producer/Consumer
│   ├── producer/
│   │   ├── CrawlingTaskProducer
│   │   └── NotificationProducer
│   └── consumer/
│       ├── CrawlingWorker
│       ├── PriceDataConsumer
│       └── NotificationWorker
├── repository/              # Data access layer
│   ├── MemberRepository
│   ├── WatchedAccommodationRepository
│   └── PriceHistoryRepository
├── entity/                  # JPA entities
│   ├── Member
│   ├── WatchedAccommodation
│   └── PriceHistory
├── dto/                     # Data transfer objects
├── exception/               # Custom exceptions
└── config/                  # Configuration classes
    ├── RedisConfig
    ├── KafkaConfig
    └── SchedulerConfig
```

---

## 🚀 단계별 로드맵

### ✅ Phase 1: 기반 구축 (완료)
- [x] Spring Boot 프로젝트 초기화
- [x] Docker Compose (MySQL)
- [x] Member, WatchedAccommodation Entity 설계
- [x] Selenium 크롤링 로직 구현
- [x] 기본 REST API 구현
- [x] 단위 테스트 작성

**학습:** Spring Boot 구조, JPA Entity 설계, Docker 기본, Selenium 크롤링

---

### 🔄 Phase 2: Redis 캐싱 (현재)
- [ ] Redis Docker Compose 추가
- [ ] Redis 연동 및 캐싱 구현
- [ ] 실시간 조회 API 완성 (캐시 적용)
- [ ] PriceHistory 저장 로직

**학습 목표:** Redis 캐싱 전략, TTL 관리, Cache-Aside 패턴

---

### 📅 Phase 3: Kafka & 스케줄러
- [ ] Kafka Docker Compose 추가
- [ ] 크롤링 작업 큐 구현 (Producer/Consumer)
- [ ] Spring Scheduler 구현 (6시간 주기)
- [ ] 멀티 Worker 병렬 처리

**학습 목표:** Kafka Producer/Consumer, 비동기 작업 큐, Spring @Scheduled

---

### 📅 Phase 4: 알림 시스템
- [ ] FCM 연동 (푸시 알림)
- [ ] SMS API 연동 (문자)
- [ ] 최저가 체크 로직
- [ ] Kafka 멀티 Consumer 패턴

**학습 목표:** 외부 API 연동, 이벤트 기반 알림

---

### 📅 Phase 5: AWS 배포 & CI/CD
- [ ] Docker 멀티 스테이지 빌드
- [ ] AWS EC2 + RDS 구축
- [ ] GitHub Actions CI/CD
- [ ] 모니터링 설정

**학습 목표:** 실전 배포, CI/CD 파이프라인

---

## 🏃 로컬 실행 방법

### Prerequisites
```bash
- Java 17+
- Docker & Docker Compose
- Chrome Browser (for Selenium)
```

### 1. 전체 인프라 실행 (PostgreSQL, Redis, Kafka, Zookeeper)
```bash
docker-compose up -d
```

### 2. Application 실행
```bash
./gradlew bootRun
```

### 3. API 테스트



## 🐳 Docker Compose 구성

```yaml
version: '3.8'
services:
  postgres:      # Main Database
  redis:         # Cache & Session
  zookeeper:     # Kafka 의존성
  kafka:         # Message Queue
  app:           # Spring Boot Application
  worker:        # Crawling Worker (scale 가능)
```

---

## 📊 Kafka Topics 설계

| Topic | 용도 | Producer | Consumer |
|-------|------|----------|----------|
| `crawling-tasks` | 크롤링 작업 큐 | API Server, Scheduler | Crawling Worker |
| `price-data` | 가격 데이터 스트림 | Crawling Worker | PriceService, PriceAnalyzer |
| `notifications` | 알림 이벤트 | PriceAnalyzer | FCM Worker, SMS Worker |

---

## 🎓 학습 포인트 정리

### 1. Spring Boot
- REST API 설계
- `@Scheduled` 스케줄러
- `@Async` 비동기 처리
- Spring Events

### 2. JPA
- Entity 연관관계 설계
- N+1 문제 해결 (`@EntityGraph`, Fetch Join)
- 배치 처리 최적화
- Native Query vs JPQL

### 3. Docker
- Multi-container 구성 (docker-compose)
- 멀티 스테이지 빌드
- Volume 관리
- Network 설정

### 4. Redis
- Cache-Aside Pattern
- TTL 전략
- Redis Pub/Sub (선택)
- Session 관리

### 5. Kafka
- Producer/Consumer 패턴
- Consumer Group
- Offset 관리
- 멀티 Worker 병렬 처리

### 6. AWS
- EC2 인스턴스 관리
- RDS 구성 및 보안
- Security Group 설정
- 비용 최적화

### 7. CI/CD
- GitHub Actions Workflow
- 자동 테스트
- Docker 이미지 빌드 & 푸시
- AWS 자동 배포

---


## 👨‍💻 개발자

- GitHub: [@fdsajklt2](https://github.com/fdsajklt2)
- Velog: [@fdsajklt2](https://velog.io/@fdsajklt2)

---

**Note**: 이 프로젝트는 학습 목적으로 개발되었으며, 실제 서비스 운영을 목적으로 하지 않습니다.
