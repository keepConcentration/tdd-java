package io.hhplus.tdd.point.domain.service;

import io.hhplus.tdd.point.domain.model.UserPoint;
import io.hhplus.tdd.point.domain.repository.UserPointRepository;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

  private final UserPointRepository userPointRepository;
  private final ConcurrentHashMap<Long, Lock> userLocks = new ConcurrentHashMap<>();

  public UserPoint read(long id) {
    return userPointRepository.read(id);
  }

  public void update(UserPoint userPoint) {
    userPointRepository.save(userPoint);
  }

  public void executeWithLock(long userId, Runnable runnable) {
    Lock lock = userLocks.computeIfAbsent(userId, id -> new ReentrantLock());
    lock.lock();
    try {
      runnable.run();
    } finally {
      lock.unlock();
    }
  }
}
