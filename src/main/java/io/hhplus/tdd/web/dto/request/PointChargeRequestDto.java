package io.hhplus.tdd.web.dto.request;

import io.hhplus.tdd.common.validation.ValidationMessages;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PointChargeRequestDto {

  @Positive(message = ValidationMessages.POSITIVE_AMOUNT)
  private long amount;
}
