package io.hhplus.tdd.point.domain.repository;

import io.hhplus.tdd.point.domain.model.PointHistory;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class PointHistoryRepository {

  public List<PointHistory> readHistories(long userId) {
    return List.of();
  }
}
