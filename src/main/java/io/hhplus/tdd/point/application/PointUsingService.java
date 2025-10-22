package io.hhplus.tdd.point.application;

import io.hhplus.tdd.point.domain.model.PointHistory;
import io.hhplus.tdd.point.domain.model.UserPoint;
import io.hhplus.tdd.point.domain.service.PointHistoryService;
import io.hhplus.tdd.point.domain.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointUsingService {

  private final PointService pointService;

  private final PointHistoryService pointHistoryService;

  public void use(long userId, long amount) {
    UserPoint userPoint = pointService.read(userId);
    UserPoint usedUserPoint = userPoint.use(amount);
    pointService.update(usedUserPoint);
    pointHistoryService.save(PointHistory.forUse(userId, amount));
  }
}
