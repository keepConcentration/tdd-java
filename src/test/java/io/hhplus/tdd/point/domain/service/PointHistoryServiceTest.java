package io.hhplus.tdd.point.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import io.hhplus.tdd.point.domain.model.PointHistory;
import io.hhplus.tdd.point.domain.model.TransactionType;
import io.hhplus.tdd.point.domain.repository.PointHistoryRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PointHistoryServiceTest {

  @InjectMocks
  private PointHistoryService pointHistoryService;

  @Mock
  private PointHistoryRepository pointHistoryRepository;

  @Test
  @DisplayName("포인트 이력을 정상적으로 조회한다.")
  void readHistories() {
    // given
    long userId = 1L;
    long currentTimestamp1 = System.currentTimeMillis();
    long currentTimestamp2 = System.currentTimeMillis();

    List<PointHistory> expectHistories = List.of(
        new PointHistory(1L, userId, 100L, TransactionType.CHARGE, currentTimestamp1),
        new PointHistory(2L, userId, 100L, TransactionType.USE, currentTimestamp2));
    when(pointHistoryRepository.readHistories(anyLong()))
        .thenReturn(expectHistories);

    // when
    List<PointHistory> histories = pointHistoryService.readHistories(userId);

    // then
    assertEquals(expectHistories, histories);
  }
}
