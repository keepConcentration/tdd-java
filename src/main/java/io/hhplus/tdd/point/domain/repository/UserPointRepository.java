package io.hhplus.tdd.point.domain.repository;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.domain.model.UserPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserPointRepository {

  private final UserPointTable userPointTable;

  public long save(UserPoint userPoint) {
    userPointTable.insertOrUpdate(userPoint.id(), userPoint.point());
    return userPoint.id();
  }

  public UserPoint read(long id) {
    return userPointTable.selectById(id);
  }
}
