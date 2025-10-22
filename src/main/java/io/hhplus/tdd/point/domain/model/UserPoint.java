package io.hhplus.tdd.point.domain.model;

import static io.hhplus.tdd.point.domain.model.PointPolicy.MAX_POINT;
import static io.hhplus.tdd.point.domain.model.PointPolicy.USE_POINT_UNIT;

import io.hhplus.tdd.common.exception.BusinessException;
import io.hhplus.tdd.common.exception.ErrorCode;

public record UserPoint(
    long id,
    long point,
    long updateMillis
) {

  public static UserPoint empty(long id) {
    return create(id, 0);
  }

  public static UserPoint of(long id, long point) {
    return create(id, point);
  }

  public UserPoint charge(long amount) {
    long newPoint = point + amount;
    if (newPoint > MAX_POINT) {
      throw new BusinessException(ErrorCode.EXCEED_MAX_POINT);
    }
    return create(id, newPoint);
  }

  public UserPoint use(long amount) {
    if (amount % USE_POINT_UNIT != 0) {
      throw new BusinessException(ErrorCode.INVALID_POINT_UNIT);
    }
    long newPoint = point - amount;
    if (newPoint < 0) {
      throw new BusinessException(ErrorCode.INSUFFICIENT_POINT);
    }
    return create(id, newPoint);
  }

  private static UserPoint create(long id, long point) {
    return new UserPoint(id, point, System.currentTimeMillis());
  }
}
