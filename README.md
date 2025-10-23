# 동시성 제어 방식 분석 보고서

## 1. 개요

동시성 제어 방식 분석 보고서입니다.

## 2. 적용한 동시성 제어 방식

### 2.1 포인트 충전 서비스 (PointChargingService)

방식: `ReentrantLock`

코드:
```java
lockManager.executeWithLock(userId, () -> {
    UserPoint userPoint = pointService.read(userId);
    UserPoint chargedPoint = userPoint.charge(amount);
    pointService.update(chargedPoint);
    pointHistoryService.save(PointHistory.forCharge(userId, amount));
});
```

`LockManager`는 `ConcurrentHashMap`에 사용자 ID 별로 `ReentrantLock` 객체를 생성해서 락을 관리합니다.

장점:
- 타임아웃, 인터럽트 등의 락 제어가 쉬움
- 공통 컴포넌트(`LockManager`)로 추상화되어 재사용이 용이함

단점:
- `synchronized`보다 코드가 복잡함
- `finally` 블록에서 `unlock()` 호출을 필요로 함
- 사용자 ID 별로 `ReentrantLock` 객체를 생성해 메모리 사용량이 상대적으로 높음

### 2.2 포인트 사용 서비스 (PointUsingService)

방식: `synchronized`

코드:
```java
private final Map<Long, Object> userLocks = new ConcurrentHashMap<>();

public void use(long userId, long amount) {
    Object lock = getUserLock(userId);
    synchronized (lock) {
        UserPoint userPoint = pointService.read(userId);
        UserPoint usedUserPoint = userPoint.use(amount);
        pointService.update(usedUserPoint);
        pointHistoryService.save(PointHistory.forUse(userId, amount));
    }
}
```

사용자 ID 별로 `Object` 객체를 생성해 `ConcurrentHashMap`에 저장한 후,
해당 `Object`와 `synchronized` 블록을 통해 동기화합니다.

장점:
- `ReentrantLock`보다 코드가 간단함
- unlock 자동 처리
- `ReentrantLock`보다 메모리 사용량이 상대적으로 낮음

단점:
- 타임아웃, 인터럽트 등 락 제어가 어려움
- 블록 단위로만 적용이 가능함
