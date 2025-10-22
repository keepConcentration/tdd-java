package io.hhplus.tdd.point.domain.model;

import static io.hhplus.tdd.point.domain.model.PointPolicy.MAX_POINT;
import static io.hhplus.tdd.point.domain.model.PointPolicy.USE_POINT_UNIT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.hhplus.tdd.common.exception.BusinessException;
import io.hhplus.tdd.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class UserPointTest {

  @Test
  @DisplayName("포인트를 충전하면 기존 포인트에 충전 금액이 더해진다.")
  void charge_ShouldAddAmountToCurrentPoint() {
    // given
    long userId = 1L;
    UserPoint userPoint = UserPoint.of(userId, 1000L);

    // when
    UserPoint charged = userPoint.charge(500L);

    // then
    assertThat(charged.point()).isEqualTo(1500L);
  }

  @Test
  @DisplayName("포인트를 사용하면 기존 포인트에서 사용 금액이 차감된다.")
  void use_ShouldSubtractAmountFromCurrentPoint() {
    // given
    long userId = 1L;
    UserPoint userPoint = UserPoint.of(userId, 1000L);

    // when
    UserPoint used = userPoint.use(USE_POINT_UNIT);

    // then
    assertThat(used.point()).isEqualTo(1000L - USE_POINT_UNIT);
  }

  @ParameterizedTest
  @ValueSource(longs = {1L, 50L, 99L, 150L, 555L})
  @DisplayName(USE_POINT_UNIT + "원 단위가 아닌 금액은 사용할 수 없다.")
  void use_ShouldReject_WhenAmountIsNotMultipleOfPointUnit(long amount) {
    // given
    long userId = 1L;
    UserPoint userPoint = UserPoint.of(userId, 1000L);

    // when & then
    assertThatThrownBy(() -> userPoint.use(amount))
        .isInstanceOf(BusinessException.class)
        .hasMessage(ErrorCode.INVALID_POINT_UNIT.getMessage());
  }

  @Test
  @DisplayName("보유 포인트보다 많은 금액을 사용할 수 없다.")
  void use_ShouldReject_WhenAmountExceedsCurrentPoint() {
    // given
    long userId = 1L;
    UserPoint userPoint = UserPoint.of(userId, USE_POINT_UNIT);

    // when & then
    assertThatThrownBy(() -> userPoint.use(USE_POINT_UNIT * 2))
        .isInstanceOf(BusinessException.class)
        .hasMessage(ErrorCode.INSUFFICIENT_POINT.getMessage());
  }

  @Test
  @DisplayName("보유 포인트를 모두 사용할 수 있다.")
  void use_ShouldSucceed_WhenAmountEqualsCurrentPoint() {
    // given
    long userId = 1L;
    UserPoint userPoint = UserPoint.of(userId, USE_POINT_UNIT * 3);

    // when
    UserPoint used = userPoint.use(USE_POINT_UNIT * 3);

    // then
    assertThat(used.point()).isZero();
  }

  @Test
  @DisplayName("최대 보유 가능 포인트는 " + MAX_POINT + "원이다.")
  void charge_ShouldReject_WhenExceedingMaxPoint() {
    // given
    long userId = 1L;
    UserPoint userPoint = UserPoint.of(userId, MAX_POINT - USE_POINT_UNIT);

    // when & then
    assertThatThrownBy(() -> userPoint.charge(USE_POINT_UNIT + 1))
        .isInstanceOf(BusinessException.class)
        .hasMessage(ErrorCode.EXCEED_MAX_POINT.getMessage());
  }

  @Test
  @DisplayName("최대 포인트까지만 충전할 수 있다.")
  void charge_ShouldSucceed_WhenResultEqualsMaxPoint() {
    // given
    long userId = 1L;
    UserPoint userPoint = UserPoint.of(userId, MAX_POINT - 100L);

    // when
    UserPoint charged = userPoint.charge(100L);

    // then
    assertThat(charged.point()).isEqualTo(MAX_POINT);
  }
}
