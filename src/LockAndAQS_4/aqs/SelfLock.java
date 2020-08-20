package LockAndAQS_4.aqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author YangYao
 * @date 2020/8/19 20:27
 * @Description 实现一个类似于ReentrantLock的锁
 */
public class SelfLock implements Lock {
    private static class Sync extends AbstractQueuedSynchronizer {
        //state表示有线程获取到锁

        //是否占用
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        protected boolean tryAcquire(int arg) {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        protected boolean tryRelease(int arg) {
            if (getState() == 0) {
                throw new UnsupportedOperationException();
            }
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        Condition newCondition() {
            return new ConditionObject();
        }
    }

    private final Sync sycn = new Sync();

    @Override
    public void lock() {
        sycn.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sycn.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sycn.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sycn.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sycn.release(1);
    }

    @Override
    public Condition newCondition() {
        return sycn.newCondition();
    }
}
