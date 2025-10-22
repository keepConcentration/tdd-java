package io.hhplus.tdd.its;

import static io.hhplus.tdd.point.domain.model.PointPolicy.USE_POINT_UNIT;
import static org.assertj.core.api.Assertions.assertThat;

import io.hhplus.tdd.point.domain.model.TransactionType;
import io.hhplus.tdd.web.dto.request.PointChargeRequestDto;
import io.hhplus.tdd.web.dto.request.PointUseRequestDto;
import io.hhplus.tdd.web.dto.response.PointHistoryResponseDto;
import io.hhplus.tdd.web.dto.response.PointResponseDto;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
class PointIntegrationTest {

  @Autowired
  private TestRestTemplate restTemplate;

  private static final long USER_ID = 1L;
  private static final long INITIAL_CHARGE_AMOUNT = 1000L;
  private static final long USE_AMOUNT = USE_POINT_UNIT;
  private static final long ADDITIONAL_CHARGE_AMOUNT = USE_AMOUNT * 2;

  @Test
  @Order(1)
  @DisplayName("초기 포인트는 0이다.")
  void read() {
    ResponseEntity<PointResponseDto> response = restTemplate.getForEntity("/points/{id}",
        PointResponseDto.class, USER_ID);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody().getPoint()).isEqualTo(0L);
  }

  @Test
  @Order(2)
  @DisplayName(INITIAL_CHARGE_AMOUNT + "포인트를 충전한다.")
  void chargeUserPoint() {
    // given
    PointChargeRequestDto request = new PointChargeRequestDto(INITIAL_CHARGE_AMOUNT);

    // when
    ResponseEntity<Void> response = restTemplate.exchange(
        "/points/{id}/charge",
        HttpMethod.PATCH,
        new HttpEntity<>(request),
        Void.class,
        USER_ID);

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    ResponseEntity<PointResponseDto> pointResponse = restTemplate.getForEntity(
        "/points/{id}", PointResponseDto.class, USER_ID);
    assertThat(pointResponse.getBody().getPoint()).isEqualTo(INITIAL_CHARGE_AMOUNT);
  }

  @Test
  @Order(3)
  @DisplayName("충전 이력이 저장된다.")
  void saveChargeHistory() {
    // when
    ResponseEntity<List<PointHistoryResponseDto>> response = restTemplate.exchange(
        "/points/{id}/histories",
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<>() {
        },
        USER_ID);

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    List<PointHistoryResponseDto> histories = response.getBody();

    assertThat(histories).hasSize(1);
    assertThat(histories.get(0).getUserId()).isEqualTo(USER_ID);
    assertThat(histories.get(0).getAmount()).isEqualTo(INITIAL_CHARGE_AMOUNT);
    assertThat(histories.get(0).getType()).isEqualTo(TransactionType.CHARGE);
  }

  @Test
  @Order(4)
  @DisplayName(USE_AMOUNT + "포인트를 사용한다.")
  void use() {
    // given
    PointUseRequestDto request = new PointUseRequestDto(USE_AMOUNT);

    // when
    ResponseEntity<Void> response = restTemplate.exchange(
        "/points/{id}/use",
        HttpMethod.PATCH,
        new HttpEntity<>(request),
        Void.class,
        USER_ID);

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    ResponseEntity<PointResponseDto> pointResponse = restTemplate.getForEntity(
        "/points/{id}", PointResponseDto.class, USER_ID);
    assertThat(pointResponse.getBody().getPoint()).isEqualTo(INITIAL_CHARGE_AMOUNT - USE_AMOUNT);
  }

  @Test
  @Order(5)
  @DisplayName("충전 이력이 남아있고, 사용 이력이 저장된다.")
  void remainChargeHistoryAndUseHistoryAreSaved() {
    // when
    ResponseEntity<List<PointHistoryResponseDto>> response = restTemplate.exchange(
        "/points/{id}/histories",
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<>() {
        },
        USER_ID);

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    List<PointHistoryResponseDto> histories = response.getBody();

    assertThat(histories).hasSize(2);

    // 충전 내역
    PointHistoryResponseDto chargeHistory = histories.stream()
        .filter(h -> h.getType() == TransactionType.CHARGE)
        .findFirst()
        .orElseThrow();
    assertThat(chargeHistory.getAmount()).isEqualTo(INITIAL_CHARGE_AMOUNT);

    // 사용 내역
    PointHistoryResponseDto useHistory = histories.stream()
        .filter(h -> h.getType() == TransactionType.USE)
        .findFirst()
        .orElseThrow();
    assertThat(useHistory.getAmount()).isEqualTo(USE_AMOUNT);
  }

  @Test
  @Order(6)
  @DisplayName(ADDITIONAL_CHARGE_AMOUNT + "포인트를 추가로 충전한다.")
  void chargeAdditionalPoints() {
    // given
    PointChargeRequestDto request = new PointChargeRequestDto(ADDITIONAL_CHARGE_AMOUNT);

    // when
    ResponseEntity<Void> response = restTemplate.exchange(
        "/points/{id}/charge",
        HttpMethod.PATCH,
        new HttpEntity<>(request),
        Void.class,
        USER_ID);

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    ResponseEntity<PointResponseDto> pointResponse = restTemplate.getForEntity(
        "/points/{id}", PointResponseDto.class, USER_ID);

    long expectedPoint = INITIAL_CHARGE_AMOUNT - USE_AMOUNT + ADDITIONAL_CHARGE_AMOUNT;
    assertThat(pointResponse.getBody().getPoint()).isEqualTo(expectedPoint);
  }

  @Test
  @Order(7)
  @DisplayName("충전/사용 이력과 현재 포인트의 일치 여부를 확인한다.")
  void validateTransactionHistoryAndPointConsistency() {
    // when - 포인트 조회
    ResponseEntity<PointResponseDto> pointResponse = restTemplate.getForEntity(
        "/points/{id}", PointResponseDto.class, USER_ID);

    // when - 포인트 이력 조회
    ResponseEntity<List<PointHistoryResponseDto>> historyResponse = restTemplate.exchange(
        "/points/{id}/histories",
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<>() {
        },
        USER_ID);

    // then - 포인트 검증
    long expectedFinalPoint = INITIAL_CHARGE_AMOUNT + ADDITIONAL_CHARGE_AMOUNT - USE_AMOUNT;
    assertThat(pointResponse.getBody().getPoint()).isEqualTo(expectedFinalPoint);

    // then - 내역 검증
    List<PointHistoryResponseDto> histories = historyResponse.getBody();
    assertThat(histories).hasSize(3);

    // 총 충전 금액 검증
    long totalCharged = histories.stream()
        .filter(h -> h.getType() == TransactionType.CHARGE)
        .mapToLong(PointHistoryResponseDto::getAmount)
        .sum();
    assertThat(totalCharged).isEqualTo(INITIAL_CHARGE_AMOUNT + ADDITIONAL_CHARGE_AMOUNT);

    // 총 사용 금액 검증
    long totalUsed = histories.stream()
        .filter(h -> h.getType() == TransactionType.USE)
        .mapToLong(PointHistoryResponseDto::getAmount)
        .sum();
    assertThat(totalUsed).isEqualTo(USE_AMOUNT);

    // 포인트 일관성 검증
    assertThat(totalCharged - totalUsed).isEqualTo(pointResponse.getBody().getPoint());
  }
}
