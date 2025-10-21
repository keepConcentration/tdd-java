package io.hhplus.tdd.point.domain.model;

public record UserPoint(
    long id,
    long point,
    long updateMillis
) {

  public static UserPoint empty(long id) {
    return new UserPoint(id, 0, System.currentTimeMillis());
  }

  public static UserPoint of(long id, long point) {
    return new UserPoint(id, point, System.currentTimeMillis());
  }

  public void charge(long amount) {

  }

}
