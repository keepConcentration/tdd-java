package io.hhplus.tdd.web;

import io.hhplus.tdd.point.application.PointChargingService;
import io.hhplus.tdd.point.application.PointHistoryReadingService;
import io.hhplus.tdd.point.application.PointReadingService;
import io.hhplus.tdd.point.application.PointUsingService;
import io.hhplus.tdd.web.dto.request.PointChargeRequestDto;
import io.hhplus.tdd.web.dto.response.PointHistoryResponseDto;
import io.hhplus.tdd.web.dto.response.PointResponseDto;
import io.hhplus.tdd.web.dto.request.PointUseRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/points")
@RequiredArgsConstructor
@Slf4j
@ResponseStatus(HttpStatus.OK)
public class PointController {

  private final PointReadingService pointReadingService;

  private final PointChargingService pointChargingService;

  private final PointUsingService pointUsingService;

  private final PointHistoryReadingService pointHistoryReadingService;

  /**
   * 특정 유저의 포인트를 조회합니다.
   */
  @GetMapping("/{id}")
  public ResponseEntity<PointResponseDto> read(
      @PathVariable @Positive(message = "사용자 ID는 0보다 커야 합니다.") long id) {
    return ResponseEntity.ok(new PointResponseDto(pointReadingService.read(id)));
  }

  /**
   * TODO - 특정 유저의 포인트 충전/이용 내역을 조회하는 기능을 작성해주세요.
   */
  @GetMapping("/{id}/histories")
  public ResponseEntity<List<PointHistoryResponseDto>> readHistories(
      @PathVariable @Positive(message = "사용자 ID는 0보다 커야 합니다.") long id) {
    return ResponseEntity.ok(pointHistoryReadingService.readHistories(id));
  }

  /**
   * 특정 유저의 포인트를 충전합니다.
   */
  @PatchMapping("/{id}/charge")
  public ResponseEntity<Void> charge(
      @PathVariable @Positive(message = "사용자 ID는 0보다 커야 합니다.") long id,
      @RequestBody @Valid PointChargeRequestDto requestDto) {
    pointChargingService.charge(id, requestDto.getAmount());
    return ResponseEntity.noContent().build();
  }

  /**
   * 특정 유저의 포인트를 사용합니다.
   */
  @PatchMapping("/{id}/use")
  public ResponseEntity<Void> use(
      @PathVariable @Min(value = 1, message = "사용자 ID는 0보다 커야 합니다.") long id,
      @RequestBody @Valid PointUseRequestDto requestDto) {
    pointUsingService.use(id, requestDto.getAmount());
    return ResponseEntity.noContent().build();
  }
}
