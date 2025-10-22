package io.hhplus.tdd.web.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PointChargeRequestDto {

  @Positive(message = "충전 금액은 0보다 커야 합니다.")
  private long amount;
}
