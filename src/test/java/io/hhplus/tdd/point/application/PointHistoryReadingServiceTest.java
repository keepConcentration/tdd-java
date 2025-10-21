package io.hhplus.tdd.point.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import io.hhplus.tdd.point.domain.model.PointHistory;
import io.hhplus.tdd.point.domain.model.TransactionType;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PointHistoryReadingServiceTest {

  @InjectMocks
  private PointHistoryReadingService pointHistoryReadingService;

  @InjectMocks
  private PointHistoryService pointHistoryService;

  @Test
  @DisplayName("포인트 이력을 정상적으로 조회한다.")
  void readHistories() {
    // given
    long userId = 1L;
    when(pointHistoryService.readHistories(userId))
        .thenReturn(List.of(
            new PointHistory(1L, userId, 100L, TransactionType.CHARGE, System.currentTimeMillis()),
            new PointHistory(2L, userId, 100L, TransactionType.USE, System.currentTimeMillis())));

    // when
    List<PointHistoryResponseDto> histories = pointHistoryReadingService.readHistories(
        userId);

    // then
    assertEquals(2, histories.size());
    assertEquals(1L, histories.get(0).getId());
    assertEquals(2L, histories.get(1).getId());
    assertEquals(userId, histories.get(0).getUserId());
    assertEquals(userId, histories.get(1).getUserId());
    assertEquals(100L, histories.get(0).getAmount());
    assertEquals(100L, histories.get(1).getAmount());
    assertEquals(TransactionType.CHARGE, histories.get(0).getType());
    assertEquals(TransactionType.USE, histories.get(1).getType());
  }
}
