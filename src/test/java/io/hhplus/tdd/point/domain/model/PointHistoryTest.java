package io.hhplus.tdd.point.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PointHistoryTest {

    @Test
    @DisplayName("충전 이력 생성 시 userId, amount, 트랜잭션 타입, id가 올바르게 설정된다")
    void forCharge_ShouldCreateHistoryWithCorrectFields() {
        // given
        long userId = 1L;
        long amount = 1000L;

        // when
        PointHistory history = PointHistory.forCharge(userId, amount);

        // then
        assertThat(history).isNotNull();
        assertThat(history.id()).isZero();
        assertThat(history.userId()).isEqualTo(userId);
        assertThat(history.amount()).isEqualTo(amount);
        assertThat(history.type()).isEqualTo(TransactionType.CHARGE);
    }

    @Test
    @DisplayName("충전 이력 생성 시 updateMillis가 현재 시간으로 설정된다")
    void forCharge_ShouldSetUpdateMillisToCurrentTime() {
        // given
        long userId = 1L;
        long amount = 1000L;
        long beforeTime = System.currentTimeMillis();

        // when
        PointHistory history = PointHistory.forCharge(userId, amount);

        // then
        long afterTime = System.currentTimeMillis();
        assertThat(history.updateMillis()).isBetween(beforeTime, afterTime);
    }
}
