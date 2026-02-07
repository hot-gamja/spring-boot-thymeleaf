---
title: AOP와 프록시 패턴 이해하기
date: 2025-06-15
tags:
  - Spring
  - AOP
  - Proxy
category: SPRING
summary: Spring AOP의 동작 원리와 프록시 패턴을 이해하고, 실무에서의 활용 방법을 정리합니다.
---

## 들어가며

Spring AOP(Aspect-Oriented Programming)는 횡단 관심사(cross-cutting concerns)를 분리하여
비즈니스 로직에 집중할 수 있게 해주는 핵심 기능입니다.

로깅, 트랜잭션 관리, 보안 체크 같은 공통 로직을 매번 메서드마다 작성하는 대신,
AOP를 통해 한 곳에서 정의하고 필요한 곳에 자동으로 적용할 수 있습니다.

## 프록시 패턴이란?

프록시 패턴은 어떤 객체에 대한 접근을 제어하기 위해 **대리 객체(Proxy)**를 제공하는 디자인 패턴입니다.

```java
public interface UserService {
    User findById(Long id);
}

public class UserServiceImpl implements UserService {
    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }
}

// 프록시: 실제 호출 전후에 로깅 추가
public class UserServiceProxy implements UserService {
    private final UserService target;

    @Override
    public User findById(Long id) {
        log.info("findById 호출: id={}", id);
        User result = target.findById(id);
        log.info("findById 완료: {}", result);
        return result;
    }
}
```

## Spring AOP의 동작 방식

Spring AOP는 기본적으로 **JDK Dynamic Proxy** 또는 **CGLIB Proxy**를 사용합니다.

| 방식 | 조건 | 특징 |
|------|------|------|
| JDK Dynamic Proxy | 인터페이스가 있을 때 | 인터페이스 기반 프록시 생성 |
| CGLIB Proxy | 인터페이스가 없을 때 | 클래스 상속 기반 프록시 생성 |

Spring Boot 2.0부터는 기본적으로 CGLIB 프록시를 사용합니다.

## @Aspect 사용 예제

```java
@Aspect
@Component
public class LoggingAspect {

    @Around("execution(* com.example.service.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long elapsed = System.currentTimeMillis() - start;
        log.info("{} 실행 시간: {}ms",
                joinPoint.getSignature().getName(), elapsed);

        return result;
    }
}
```

## 주의사항

1. **Self-invocation 문제**: 같은 클래스 내부에서 메서드를 호출하면 프록시를 거치지 않습니다.
2. **private 메서드**: AOP가 적용되지 않습니다.
3. **final 클래스/메서드**: CGLIB 프록시를 사용할 수 없습니다.

## 마무리

AOP와 프록시 패턴을 이해하면 Spring의 트랜잭션(`@Transactional`), 캐싱(`@Cacheable`),
비동기(`@Async`) 등의 동작 원리를 깊이 이해할 수 있습니다.
