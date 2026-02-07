---
title: Java Stream API 완전 정복
date: 2025-07-01
tags:
  - Java
  - Stream API
  - Functional Programming
category: JAVA
summary: Java 8에서 도입된 Stream API의 핵심 개념과 실전 활용법을 정리합니다.
---

## Stream API란?

Java 8에서 도입된 Stream API는 컬렉션 데이터를 **선언적(declarative)**으로 처리할 수 있게 해줍니다.
기존의 반복문 대신 파이프라인 방식으로 데이터를 변환, 필터링, 집계할 수 있습니다.

## 기본 사용법

```java
List<String> names = List.of("Alice", "Bob", "Charlie", "David", "Eve");

// 기존 방식 (명령형)
List<String> result = new ArrayList<>();
for (String name : names) {
    if (name.length() > 3) {
        result.add(name.toUpperCase());
    }
}

// Stream 방식 (선언형)
List<String> result = names.stream()
        .filter(name -> name.length() > 3)
        .map(String::toUpperCase)
        .collect(Collectors.toList());
// [ALICE, CHARLIE, DAVID]
```

## 주요 중간 연산

| 메서드 | 설명 | 예시 |
|--------|------|------|
| `filter` | 조건에 맞는 요소만 통과 | `.filter(x -> x > 0)` |
| `map` | 요소를 변환 | `.map(String::toUpperCase)` |
| `flatMap` | 중첩 구조를 평탄화 | `.flatMap(List::stream)` |
| `distinct` | 중복 제거 | `.distinct()` |
| `sorted` | 정렬 | `.sorted(Comparator.reverseOrder())` |
| `limit` | 개수 제한 | `.limit(5)` |
| `skip` | 앞에서 N개 건너뛰기 | `.skip(2)` |

## 주요 최종 연산

```java
// collect: 결과를 컬렉션으로 수집
List<String> list = stream.collect(Collectors.toList());
Map<String, List<User>> grouped = users.stream()
        .collect(Collectors.groupingBy(User::getDepartment));

// reduce: 요소를 하나로 합침
int sum = numbers.stream()
        .reduce(0, Integer::sum);

// forEach: 각 요소에 대해 작업 수행
names.stream().forEach(System.out::println);

// count, min, max
long count = stream.count();
Optional<Integer> max = numbers.stream().max(Integer::compareTo);
```

## 실전 예제: 주문 데이터 분석

```java
// 카테고리별 매출 합계
Map<String, Integer> salesByCategory = orders.stream()
        .collect(Collectors.groupingBy(
                Order::getCategory,
                Collectors.summingInt(Order::getAmount)
        ));

// 상위 3개 고객 (구매 금액 기준)
List<String> topCustomers = orders.stream()
        .collect(Collectors.groupingBy(
                Order::getCustomerName,
                Collectors.summingInt(Order::getAmount)))
        .entrySet().stream()
        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
        .limit(3)
        .map(Map.Entry::getKey)
        .collect(Collectors.toList());
```

## 주의사항

1. **Stream은 일회용입니다.** 한 번 소비된 스트림은 재사용할 수 없습니다.
2. **병렬 스트림(`parallelStream`)** 은 항상 빠른 것이 아닙니다. 데이터가 충분히 많을 때만 사용하세요.
3. **부작용(side-effect)을 피하세요.** `forEach` 안에서 외부 상태를 변경하면 예측하기 어려운 버그가 생길 수 있습니다.

## 마무리

Stream API를 잘 활용하면 코드가 간결해지고 의도가 명확해집니다.
처음에는 낯설 수 있지만, 익숙해지면 컬렉션 처리에서 빠질 수 없는 도구가 됩니다.
