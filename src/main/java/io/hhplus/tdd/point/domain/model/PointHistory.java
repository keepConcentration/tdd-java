package io.hhplus.tdd.point.domain.model;

public record PointHistory(
    long id,
    long userId,
    long amount,
    TransactionType type,
    long updateMillis
) {

  public static PointHistory forCharge(long userId, long amount) {
    return create(userId, amount, TransactionType.CHARGE);
  }

  public static PointHistory forUse(long userId, long amount) {
    return create(userId, amount, TransactionType.USE);
  }

  private static PointHistory create(long userId, long amount, TransactionType type) {
    return new PointHistory(
        0L,
        userId,
        amount,
        type,
        System.currentTimeMillis()
    );
  }
}
