package io.hhplus.tdd.web;

import io.hhplus.tdd.point.domain.model.PointHistory;
import io.hhplus.tdd.point.domain.model.UserPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/points")
@RequiredArgsConstructor
@Slf4j
@ResponseStatus(HttpStatus.OK)
public class PointController {

  /**
   * TODO - 특정 유저의 포인트를 조회하는 기능을 작성해주세요.
   */
  @GetMapping("/{id}")
  public UserPoint read(@PathVariable long id) {
    return new UserPoint(0, 0, 0);
  }

  /**
   * TODO - 특정 유저의 포인트 충전/이용 내역을 조회하는 기능을 작성해주세요.
   */
  @GetMapping("/{id}/histories")
  public List<PointHistory> readHistories(@PathVariable long id) {
    return List.of();
  }

  /**
   * TODO - 특정 유저의 포인트를 충전하는 기능을 작성해주세요.
   */
  @PatchMapping("/{id}/charge")
  public void charge(@PathVariable long id, @RequestBody long amount) {

  }

  /**
   * TODO - 특정 유저의 포인트를 사용하는 기능을 작성해주세요.
   */
  @PatchMapping("/{id}/use")
  public void use(@PathVariable long id, @RequestBody long amount) {

  }
}
