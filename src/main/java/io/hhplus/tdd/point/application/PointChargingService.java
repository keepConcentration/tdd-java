package io.hhplus.tdd.point.application;

import io.hhplus.tdd.point.domain.model.PointHistory;
import io.hhplus.tdd.point.domain.model.UserPoint;
import io.hhplus.tdd.point.domain.service.PointHistoryService;
import io.hhplus.tdd.point.domain.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointChargingService {

  private final PointService pointService;

  private final PointHistoryService pointHistoryService;

  public void charge(long userId, long amount) {
    pointService.executeWithLock(userId, () -> {
      UserPoint userPoint = pointService.read(userId);
      UserPoint chargedPoint = userPoint.charge(amount);

      pointService.update(chargedPoint);

      pointHistoryService.save(PointHistory.forCharge(userId, amount));
    });
  }
}
