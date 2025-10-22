package io.hhplus.tdd.common.validation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidationMessages {

  public static final String POSITIVE_USER_ID = "사용자 ID는 0보다 커야 합니다.";
  public static final String POSITIVE_AMOUNT = "금액은 0보다 커야 합니다.";

}
