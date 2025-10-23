package io.hhplus.tdd.point.application;

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
class PointUsingServiceTest {

  @InjectMocks
  private PointUsingService pointUsingService;

  @Mock
  private PointService pointService;

  @Mock
  private PointHistoryService pointHistoryService;

  @Test
  @DisplayName("포인트를 정상적으로 사용한다.")
  void use() {
    // given
    long userId = 1L;
    long amount = 100L;
    UserPoint userPoint = mock(UserPoint.class);
    when(pointService.read(userId))
        .thenReturn(userPoint);

    // when
    pointUsingService.use(userId, amount);

    // then
    verify(pointService, times(1)).update(any());
    verify(userPoint, times(1)).use(anyLong());
  }

  @Test
  @DisplayName("포인트 사용 시 포인트 변경 이력 정보를 저장한다.")
  void useAndSaveHistory() {
    // given
    long userId = 1L;
    long amount = 100L;
    UserPoint userPoint = mock(UserPoint.class);
    when(pointService.read(anyLong()))
        .thenReturn(userPoint);

    // when
    pointUsingService.use(userId, amount);

    // then
    verify(pointHistoryService, times(1)).save(any());
  }
}
