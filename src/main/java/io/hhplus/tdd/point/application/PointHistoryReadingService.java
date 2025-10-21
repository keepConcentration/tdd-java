package io.hhplus.tdd.point.application;

import io.hhplus.tdd.point.domain.model.PointHistory;
import io.hhplus.tdd.point.domain.service.PointHistoryService;
import io.hhplus.tdd.web.dto.response.PointHistoryResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointHistoryReadingService {

  private final PointHistoryService pointHistoryService;

  public List<PointHistoryResponseDto> readHistories(long id) {
    List<PointHistory> pointHistories = pointHistoryService.readHistories(id);
    return pointHistories.stream()
        .map(PointHistoryResponseDto::of)
        .toList();
  }
}
