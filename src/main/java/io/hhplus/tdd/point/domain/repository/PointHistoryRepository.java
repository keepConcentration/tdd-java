package io.hhplus.tdd.point.domain.repository;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.point.domain.model.PointHistory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointHistoryRepository {

  private final PointHistoryTable pointHistoryTable;

  public List<PointHistory> readHistories(long userId) {
    return pointHistoryTable.selectAllByUserId(userId);
  }

  public PointHistory save(PointHistory pointHistory) {
    return pointHistoryTable.insert(
        pointHistory.userId(),
        pointHistory.amount(),
        pointHistory.type(),
        pointHistory.updateMillis()
    );
  }
}
