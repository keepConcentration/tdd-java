package io.hhplus.tdd.web;

import io.hhplus.tdd.point.application.PointChargingService;
import io.hhplus.tdd.point.application.PointReadingService;
import io.hhplus.tdd.point.domain.model.PointHistory;
import io.hhplus.tdd.web.dto.PointChargeRequestDto;
import io.hhplus.tdd.web.dto.PointResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/points")
@RequiredArgsConstructor
@Slf4j
@ResponseStatus(HttpStatus.OK)
public class PointController {

  private final PointReadingService pointReadingService;

  private final PointChargingService pointChargingService;

  /**
   * 특정 유저의 포인트를 조회합니다.
   */
  @GetMapping("/{id}")
  public ResponseEntity<PointResponseDto> read(@PathVariable long id) {
    return ResponseEntity.ok(new PointResponseDto(pointReadingService.read(id)));
  }

  /**
   * TODO - 특정 유저의 포인트 충전/이용 내역을 조회하는 기능을 작성해주세요.
   */
  @GetMapping("/{id}/histories")
  public List<PointHistory> readHistories(@PathVariable long id) {
    return List.of();
  }

  /**
   * 특정 유저의 포인트를 충전합니다.
   */
  @PatchMapping("/{id}/charge")
  public ResponseEntity<Void> charge(@PathVariable long id, @RequestBody PointChargeRequestDto requestDto) {
    pointChargingService.charge(id, requestDto.getAmount());
    return ResponseEntity.noContent().build();
  }

  /**
   * TODO - 특정 유저의 포인트를 사용하는 기능을 작성해주세요.
   */
  @PatchMapping("/{id}/use")
  public void use(@PathVariable long id, @RequestBody long amount) {

  }
}
