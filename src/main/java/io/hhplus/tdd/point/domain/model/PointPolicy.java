package io.hhplus.tdd.point.domain.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PointPolicy {

  public static final long MAX_POINT = 10_000_000L;
  public static final long POINT_UNIT = 100L;
}
