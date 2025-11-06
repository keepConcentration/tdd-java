package io.hhplus.tdd.common.concurrency;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.stereotype.Component;

@Component
public class LockManager {

  private final Map<Long, Lock> locks = new ConcurrentHashMap<>();

  private Lock getLock(long key) {
    return locks.computeIfAbsent(key, lockKey -> new ReentrantLock());
  }

  public void executeWithLock(long key, Runnable runnable) {
    Lock lock = getLock(key);
    lock.lock();
    try {
      runnable.run();
    } finally {
      lock.unlock();
    }
  }
}
