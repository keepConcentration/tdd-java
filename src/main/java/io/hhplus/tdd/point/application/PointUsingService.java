package io.hhplus.tdd.point.application;

import io.hhplus.tdd.point.domain.model.PointHistory;
import io.hhplus.tdd.point.domain.model.UserPoint;
import io.hhplus.tdd.point.domain.service.PointHistoryService;
import io.hhplus.tdd.point.domain.service.PointService;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointUsingService {

  private final PointService pointService;

  private final PointHistoryService pointHistoryService;

  private final Map<Long, Object> userLocks = new ConcurrentHashMap<>();

  private Object getUserLock(long userId) {
    return userLocks.computeIfAbsent(userId, id -> new Object());
  }

  public void use(long userId, long amount) {
    Object lock = getUserLock(userId);
    synchronized (lock) {
      UserPoint userPoint = pointService.read(userId);
      UserPoint usedUserPoint = userPoint.use(amount);
      pointService.update(usedUserPoint);
      pointHistoryService.save(PointHistory.forUse(userId, amount));
    }
  }
}
