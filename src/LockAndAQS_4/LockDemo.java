package LockAndAQS_4;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author YangYao
 * @date 2020/8/19 11:29
 * @Description
 */
public class LockDemo {
    private Lock lock=new ReentrantLock();
    private int count=0;

    public void increament(){
        lock.lock();
        try {
            count++;
        }finally {
            lock.unlock();
        }
    }

    public synchronized void incr2(){
        count++;
        incr2();//递归调用，就只能用可重入锁
    }
}
