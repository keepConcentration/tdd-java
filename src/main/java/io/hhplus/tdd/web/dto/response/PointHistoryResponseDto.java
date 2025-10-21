package io.hhplus.tdd.web.dto.response;

import io.hhplus.tdd.point.domain.model.PointHistory;
import io.hhplus.tdd.point.domain.model.TransactionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PointHistoryResponseDto {

  private long id;
  private long userId;
  private long amount;
  private TransactionType type;
  private long updateMillis;

  public static PointHistoryResponseDto of(PointHistory pointHistory) {
    return new PointHistoryResponseDto(pointHistory.id(), pointHistory.userId(), pointHistory.amount(), pointHistory.type(), pointHistory.updateMillis());
  }
}
