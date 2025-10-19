package io.hhplus.tdd.point.domain.repository;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.domain.model.UserPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserPointRepository {

  private final UserPointTable userPointTable;

  public UserPoint read(long id) {
    return userPointTable.selectById(id);
  }
}
