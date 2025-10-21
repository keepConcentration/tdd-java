package io.hhplus.tdd.point.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import io.hhplus.tdd.point.domain.model.UserPoint;
import io.hhplus.tdd.point.domain.service.PointService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PointReadingServiceTest {

  @InjectMocks
  private PointReadingService pointReadingService;

  @Mock
  private PointService pointService;

  @Test
  @DisplayName("특정 회원의 포인트를 정상적으로 조회한다.")
  void read() {
    // given
    long userId = 1L;
    UserPoint userPoint = UserPoint.of(userId, 100L);
    when(pointService.read(anyLong()))
        .thenReturn(userPoint);

    // when
    long point = pointReadingService.read(userId);

    // then
    assertEquals(userPoint.point(), point);
  }
}