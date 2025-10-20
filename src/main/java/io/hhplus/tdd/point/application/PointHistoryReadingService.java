package io.hhplus.tdd.point.application;

import io.hhplus.tdd.web.dto.response.PointHistoryResponseDto;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PointHistoryReadingService {

  public List<PointHistoryResponseDto> readHistories(long id) {
    return List.of();
  }
}
