package io.hhplus.tdd.point.domain.repository;

import static org.junit.jupiter.api.Assertions.*;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.point.domain.model.PointHistory;
import io.hhplus.tdd.point.domain.model.TransactionType;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PointHistoryRepositoryTest {

  private PointHistoryTable pointHistoryTable;

  private PointHistoryRepository pointHistoryRepository;

  @BeforeEach
  void setUp() {
    pointHistoryTable = new PointHistoryTable();
    pointHistoryRepository = new PointHistoryRepository(pointHistoryTable);
  }

  @Test
  @DisplayName("userId에 해당하는 포인트 이력을 정상적으로 조회한다.")
  void readHistories() {
    // given
    long userId = 1L;
    long currentTimestamp1 = System.currentTimeMillis();
    long currentTimestamp2 = System.currentTimeMillis() + 1000L;

    PointHistory history1 = new PointHistory(1L, userId, 100L, TransactionType.CHARGE, currentTimestamp1);
    PointHistory history2 = new PointHistory(2L, userId, 100L, TransactionType.USE, currentTimestamp2);

    pointHistoryTable.insert(history1.userId(), history1.amount(), history1.type(), history1.updateMillis());
    pointHistoryTable.insert(history2.userId(), history2.amount(), history2.type(), history2.updateMillis());

    // when
    List<PointHistory> pointHistories = pointHistoryRepository.readHistories(userId);

    // then
    assertEquals(List.of(history1, history2), pointHistories);
  }

  @Test
  @DisplayName("포인트 이력을 정상적으로 저장한다.")
  void save() {
    // given
    long userId = 1L;
    long amount = 1000L;
    PointHistory pointHistory = PointHistory.forCharge(userId, amount);

    // when
    PointHistory savedHistory = pointHistoryRepository.save(pointHistory);

    // then
    assertNotNull(savedHistory);
    assertEquals(userId, savedHistory.userId());
    assertEquals(amount, savedHistory.amount());
    assertEquals(TransactionType.CHARGE, savedHistory.type());
    assertTrue(savedHistory.id() > 0);
  }
}
