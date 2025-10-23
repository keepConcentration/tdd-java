package io.hhplus.tdd.common.concurrency;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.stereotype.Component;

@Component
public class LockManager {

  private final Map<Long, Lock> locks = new ConcurrentHashMap<>();

  private Lock getLock(long id) {
    return locks.computeIfAbsent(id, lockId -> new ReentrantLock());
  }

  public void executeWithLock(long id, Runnable runnable) {
    Lock lock = getLock(id);
    lock.lock();
    try {
      runnable.run();
    } finally {
      lock.unlock();
    }
  }
}
