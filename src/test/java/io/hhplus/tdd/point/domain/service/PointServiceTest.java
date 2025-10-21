package io.hhplus.tdd.point.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.hhplus.tdd.point.domain.model.UserPoint;
import io.hhplus.tdd.point.domain.repository.UserPointRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

  @InjectMocks
  private PointService pointService;

  @Mock
  private UserPointRepository userPointRepository;

  @Test
  @DisplayName("특정 회원의 포인트를 조회한다.")
  void read() {
    // given
    long userId = 1L;
    UserPoint userPoint = UserPoint.of(userId, 100L);
    when(userPointRepository.read(anyLong()))
        .thenReturn(userPoint);

    // when
    UserPoint readUserPoint = pointService.read(userId);

    // then
    assertEquals(userPoint, readUserPoint);
  }

  @Test
  @DisplayName("특정 회원의 포인트를 수정한다.")
  void update() {
    // given and when
    pointService.update(any());

    // then
    verify(userPointRepository, times(1)).save(any());
  }
}
