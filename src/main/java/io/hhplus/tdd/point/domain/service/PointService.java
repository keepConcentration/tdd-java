package io.hhplus.tdd.point.domain.service;

import io.hhplus.tdd.point.domain.model.UserPoint;
import io.hhplus.tdd.point.domain.repository.UserPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

  private final UserPointRepository userPointRepository;

  public UserPoint read(long id) {
    return userPointRepository.read(id);
  }
}
