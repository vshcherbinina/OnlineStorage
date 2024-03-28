package onlinestore.inventoryservice.model.util;

import onlinestore.inventoryservice.model.entity.InventoryDocumentEntity;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class LockByKey {
    private static class LockWrapper {
        private final Lock lock = new ReentrantLock();
        private final AtomicInteger numberOfThreadsInQueue = new AtomicInteger(1);

        private LockWrapper addThreadInQueue() {
            numberOfThreadsInQueue.incrementAndGet();
            return this;
        }

        private int removeThreadFromQueue() {
            return numberOfThreadsInQueue.decrementAndGet();
        }

    }

    private static final ConcurrentHashMap<Long, LockWrapper> locks = new ConcurrentHashMap<>();

    public void lock(Long key) {
        LockWrapper lockWrapper = locks.compute(key, (k, v) -> v == null ? new LockWrapper() : v.addThreadInQueue());
        lockWrapper.lock.lock();
    }

    public void unlock(Long key) {
        LockWrapper lockWrapper = locks.get(key);
        lockWrapper.lock.unlock();
        if (lockWrapper.removeThreadFromQueue() == 0) {
            locks.remove(key, lockWrapper);
        }
    }

    public void lockAllForDocument(InventoryDocumentEntity document){
        document.getDetails().forEach(detail -> lock(detail.getProduct().getId()));
    }

    public void unlockAllForDocument(InventoryDocumentEntity document){
        document.getDetails().forEach(detail -> unlock(detail.getProduct().getId()));
    }


}
