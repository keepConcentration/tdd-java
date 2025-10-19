package io.hhplus.tdd.point.application;

import io.hhplus.tdd.point.domain.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointReadingService {

  private final PointService PointService;

  public long read(long id) {
    return PointService.read(id).point();
  }
}
