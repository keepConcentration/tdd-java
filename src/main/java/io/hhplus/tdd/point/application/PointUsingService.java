package io.hhplus.tdd.point.application;

import io.hhplus.tdd.point.domain.model.UserPoint;
import io.hhplus.tdd.point.domain.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointUsingService {

  private final PointService pointService;

  public void use(long userId, long amount) {
    UserPoint userPoint = pointService.read(userId);
    pointService.update(UserPoint.of(userId, userPoint.point() - amount));
  }
}
