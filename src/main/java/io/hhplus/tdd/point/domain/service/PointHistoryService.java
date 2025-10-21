package io.hhplus.tdd.point.domain.service;

import io.hhplus.tdd.point.domain.model.PointHistory;
import io.hhplus.tdd.point.domain.repository.PointHistoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointHistoryService {

  private final PointHistoryRepository pointHistoryRepository;

  public List<PointHistory> readHistories(long userId) {
    return pointHistoryRepository.readHistories(userId);
  }
}
