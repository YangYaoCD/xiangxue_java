package LockAndAQS_4.readAndWrite;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Union;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author YangYao
 * @date 2020/8/22 15:23
 * @Description  测试读写锁升级降级
 */
public class MyDemo {
    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        ReadWriteLock rtLock = new ReentrantReadWriteLock();
        int ca=1;
        rtLock.writeLock().lock();//这个锁是可重入锁，同一线程可以拿多次写锁所以case 2不会阻塞，case 1写锁降级也不会阻塞
        switch (ca){
            case 1:
                //会造成死锁（先读再写）
                rtLock.readLock().lock();
                System.out.println("get readLock.");
                rtLock.readLock().unlock();
                rtLock.writeLock().lock();
                System.out.println("blocking");
                break;
            case 2:
                //不会造成死锁（先写再读,写锁降级）
                rtLock.writeLock().lock();
                System.out.println("writeLock");
                rtLock.readLock().lock();
                System.out.println("get read lock");
                break;
        }

        Thread.sleep(2000);
        long end=System.currentTimeMillis();
        System.out.println(end-start);
        System.out.print("继续");

    }
}
