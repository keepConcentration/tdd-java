package io.hhplus.tdd.point.domain.model;

public record PointHistory(
    long id,
    long userId,
    long amount,
    TransactionType type,
    long updateMillis
) {

  public static PointHistory forCharge(long userId, long amount) {
    return null;
  }
}
