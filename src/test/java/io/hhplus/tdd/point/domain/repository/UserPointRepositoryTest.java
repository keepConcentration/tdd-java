package io.hhplus.tdd.point.domain.repository;

import static org.junit.jupiter.api.Assertions.*;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.domain.model.UserPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserPointRepositoryTest {

  @Test
  @DisplayName("특정 회원의 포인트를 올바르게 조회한다.")
  void read() {
    // given
    long id = 1L;
    long amount = 100L;
    UserPointTable userPointTable = new UserPointTable();
    userPointTable.insertOrUpdate(id, amount);

    UserPointRepository userPointRepository = new UserPointRepository(userPointTable);

    // when
    UserPoint userPoint = userPointRepository.read(id);

    // then
    assertNotNull(userPoint);
    assertEquals(id, userPoint.id());
    assertEquals(amount, userPoint.point());
  }

  @Test
  @DisplayName("저장 후 조회했을 때 같은 Point를 반환한다.")
  void saveAndRead() {
    // given
    long id = 1L;
    long amount = 100L;
    UserPointTable userPointTable = new UserPointTable();
    UserPointRepository userPointRepository = new UserPointRepository(userPointTable);

    UserPoint savedUserPoint = UserPoint.of(id, amount);
    userPointRepository.save(savedUserPoint);

    // when
    UserPoint userPoint = userPointRepository.read(id);

    // then
    assertNotNull(userPoint);
    assertEquals(savedUserPoint.id(), userPoint.id());
    assertEquals(savedUserPoint.point(), userPoint.point());


  }
}
