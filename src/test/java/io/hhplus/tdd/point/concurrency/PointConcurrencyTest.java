package io.hhplus.tdd.point.concurrency;

import static io.hhplus.tdd.point.domain.model.PointPolicy.USE_POINT_UNIT;
import static org.assertj.core.api.Assertions.assertThat;

import io.hhplus.tdd.point.application.PointChargingService;
import io.hhplus.tdd.point.application.PointReadingService;
import io.hhplus.tdd.point.application.PointUsingService;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PointConcurrencyTest {

  @Autowired
  private PointChargingService pointChargingService;

  @Autowired
  private PointUsingService pointUsingService;

  @Autowired
  private PointReadingService pointReadingService;

  @Test
  @DisplayName("같은 사용자의 포인트를 동시에 여러번 충전했을 때 포인트 잔액이 일치해야한다.")
  void testConcurrentPointCharging() {
    // given
    long userId = 1L;
    long amount = USE_POINT_UNIT;
    int concurrentRequests = 100;

    ExecutorService executor = Executors.newFixedThreadPool(concurrentRequests);

    // when
    CompletableFuture<Void>[] futures = new CompletableFuture[concurrentRequests];
    for (int i = 0; i < concurrentRequests; i++) {
      futures[i] = CompletableFuture.runAsync(() -> pointChargingService.charge(userId, amount), executor);
    }
    CompletableFuture.allOf(futures).join();
    executor.shutdown();

    // then
    long expectedPoint = amount * concurrentRequests;
    long actualPoint = pointReadingService.read(userId);

    assertThat(actualPoint).isEqualTo(expectedPoint);
  }

  @Test
  @DisplayName("같은 사용자의 포인트를 잔액이 부족할만큼 동시에 여러번 사용했을 때 잔액이 부족해야한다.")
  void concurrentPointUsing() {
    // given
    long userId = 2L;
    long initialCharge = USE_POINT_UNIT * 100;
    long useAmount = USE_POINT_UNIT;
    int useRequests = 100;

    pointChargingService.charge(userId, initialCharge);

    ExecutorService executor = Executors.newFixedThreadPool(useRequests);

    // when
    CompletableFuture<Void>[] futures = new CompletableFuture[useRequests];
    for (int i = 0; i < useRequests; i++) {
      futures[i] = CompletableFuture.runAsync(() -> {
        try {
          pointUsingService.use(userId, useAmount);
        } catch (Exception ignored) {

        }
      }, executor);
    }

    CompletableFuture.allOf(futures).join();
    executor.shutdown();

    // then
    long finalPoint = pointReadingService.read(userId);
    assertThat(finalPoint).isEqualTo(0L);
  }

  @Test
  @DisplayName("서로 다른 사용자의 포인트 작업은 서로 독립적어야 한다.")
  void concurrentDifferentUsers() {
    // given
    long user1Id = 3L;
    long user2Id = 4L;
    long chargeAmount = USE_POINT_UNIT;
    int requestsPerUser = 5;

    ExecutorService executor = Executors.newFixedThreadPool(10);

    // when
    CompletableFuture<Void>[] user1Futures = new CompletableFuture[requestsPerUser];
    CompletableFuture<Void>[] user2Futures = new CompletableFuture[requestsPerUser];

    for (int i = 0; i < requestsPerUser; i++) {
      user1Futures[i] = CompletableFuture.runAsync(() -> {
        pointChargingService.charge(user1Id, chargeAmount);
      }, executor);

      user2Futures[i] = CompletableFuture.runAsync(() -> {
        pointChargingService.charge(user2Id, chargeAmount);
      }, executor);
    }

    CompletableFuture.allOf(user1Futures).join();
    CompletableFuture.allOf(user2Futures).join();
    executor.shutdown();

    // then
    long expectedPoint = chargeAmount * requestsPerUser;
    long user1Point = pointReadingService.read(user1Id);
    long user2Point = pointReadingService.read(user2Id);

    assertThat(user1Point).isEqualTo(expectedPoint);
    assertThat(user2Point).isEqualTo(expectedPoint);
  }
}
