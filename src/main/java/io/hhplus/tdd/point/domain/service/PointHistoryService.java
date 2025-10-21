package io.hhplus.tdd.point.domain.service;

import io.hhplus.tdd.point.domain.model.PointHistory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointHistoryService {

  public List<PointHistory> readHistories(long userId) {
    return List.of();
  }
}
