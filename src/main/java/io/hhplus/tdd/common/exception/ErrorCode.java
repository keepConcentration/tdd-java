package io.hhplus.tdd.common.exception;

import static io.hhplus.tdd.point.domain.model.PointPolicy.MAX_POINT;
import static io.hhplus.tdd.point.domain.model.PointPolicy.POINT_UNIT;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  INVALID_POINT_UNIT(HttpStatus.BAD_REQUEST, "포인트는 " + POINT_UNIT + "원 단위로만 충전/사용 가능합니다."),
  INVALID_POINT_AMOUNT(HttpStatus.BAD_REQUEST, "포인트 금액은 0보다 커야 합니다."),
  INSUFFICIENT_POINT(HttpStatus.BAD_REQUEST, "포인트는 음수가 될 수 없습니다."),
  EXCEED_MAX_POINT(HttpStatus.BAD_REQUEST, "포인트는 최대 " + MAX_POINT + "원까지 보유할 수 있습니다.");

  private final HttpStatus httpStatus;
  private final String message;
}
