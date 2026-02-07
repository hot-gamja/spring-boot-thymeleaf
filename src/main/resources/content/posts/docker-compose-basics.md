---
title: Docker Compose 기초부터 실전까지
date: 2025-05-20
tags:
  - Docker
  - Docker Compose
  - DevOps
category: DOCKER
summary: Docker Compose의 기본 개념과 멀티 컨테이너 환경 구성법을 단계별로 알아봅니다.
---

## Docker Compose란?

Docker Compose는 여러 컨테이너로 구성된 애플리케이션을 **하나의 YAML 파일**로 정의하고
관리할 수 있게 해주는 도구입니다.

예를 들어, Spring Boot 앱 + PostgreSQL + Redis를 한 번에 실행할 수 있습니다.

## 기본 구조

`docker-compose.yml` 파일의 기본 구조는 다음과 같습니다:

```yaml
version: '3.8'

services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      SPRING_PROFILES_ACTIVE: dev
    depends_on:
      - db

  db:
    image: postgres:15
    environment:
      POSTGRES_DB: mydb
      POSTGRES_USER: user
      POSTGRES_PASSWORD: password
    volumes:
      - postgres-data:/var/lib/postgresql/data
    ports:
      - "5432:5432"

volumes:
  postgres-data:
```

## 주요 명령어

```bash
# 전체 서비스 시작 (백그라운드)
docker-compose up -d

# 로그 확인
docker-compose logs -f

# 특정 서비스만 재시작
docker-compose restart app

# 전체 서비스 중지 및 컨테이너 제거
docker-compose down

# 볼륨까지 함께 제거
docker-compose down -v
```

## 환경 변수 관리

`.env` 파일을 사용하면 민감한 정보를 분리할 수 있습니다:

```
# .env
DB_PASSWORD=secret123
DB_NAME=production_db
```

```yaml
# docker-compose.yml
services:
  db:
    environment:
      POSTGRES_PASSWORD: ${DB_PASSWORD}
      POSTGRES_DB: ${DB_NAME}
```

## 개발/운영 환경 분리

override 파일을 활용하면 환경별 설정을 분리할 수 있습니다:

```bash
# 개발 환경 (기본 + override)
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up

# 운영 환경
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up
```

## 마무리

Docker Compose를 활용하면 복잡한 멀티 컨테이너 환경을 간단하게 관리할 수 있습니다.
특히 로컬 개발 환경 구성에 매우 유용하며, 팀원들과 동일한 환경을 공유할 수 있다는 점이 큰 장점입니다.
