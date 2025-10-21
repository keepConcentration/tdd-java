package io.hhplus.tdd.point.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.hhplus.tdd.point.domain.model.UserPoint;
import io.hhplus.tdd.point.domain.service.PointHistoryService;
import io.hhplus.tdd.point.domain.service.PointService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PointChargingServiceTest {

  @InjectMocks
  private PointChargingService pointChargingService;

  @Mock
  private PointService pointService;

  @Mock
  private PointHistoryService pointHistoryService;

  @Test
  @DisplayName("포인트를 정상적으로 충전한다.")
  void charge() {
    // given
    long userId = 1L;
    long amount = 100L;
    UserPoint userPoint = mock(UserPoint.class);
    when(pointService.read(anyLong()))
        .thenReturn(userPoint);

    // when
    pointChargingService.charge(userId, amount);

    // then
    verify(pointService, times(1)).update(any());
    verify(userPoint, times(1)).charge(anyLong());
  }

  @Test
  @DisplayName("포인트를 정상적으로 충전 시 포인트 변경 이력 정보를 저장한다.")
  void chargeAndSaveHistory() {
    // given
    long userId = 1L;
    long amount = 100L;
    when(pointService.read(anyLong()))
        .thenReturn(mock(UserPoint.class));

    // when
    pointChargingService.charge(userId, amount);

    // then
    verify(pointHistoryService, times(1)).save(any());
  }
}