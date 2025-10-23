package io.hhplus.tdd.point.domain.service;

import io.hhplus.tdd.point.domain.model.UserPoint;
import io.hhplus.tdd.point.domain.repository.UserPointRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

  private final UserPointRepository userPointRepository;

  private final Map<Long, Lock> userLocks = new ConcurrentHashMap<>();

  public UserPoint read(long id) {
    return userPointRepository.read(id);
  }

  public void update(UserPoint userPoint) {
    userPointRepository.save(userPoint);
  }

  private Lock getUserLock(long id) {
    return userLocks.computeIfAbsent(id, lockId -> new ReentrantLock());
  }

  public void executeWithLock(long id, Runnable runnable) {
    Lock lock = getUserLock(id);
    lock.lock();
    try {
      runnable.run();
    } finally {
      lock.unlock();
    }
  }
}
