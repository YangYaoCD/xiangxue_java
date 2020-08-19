package CAS_3;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author YangYao
 * @date 2020/8/19 9:55
 * @Description AtomicStampedReference的操作使用，版本号解决ABA问题
 */
public class UseAtomicStampedReference {
    static AtomicStampedReference<String> asr=new AtomicStampedReference<>("Mark",0);

    public static void main(String[] args) throws InterruptedException {
        final int oldStamp=asr.getStamp();//拿初始版本号
        final String oldReferenc=asr.getReference();
        System.out.println(oldReferenc+"==="+oldStamp);
        Thread rightStampThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName()+"当前变量值："+oldReferenc+"当前版本戳："+oldStamp+"-"
                +asr.compareAndSet(oldReferenc,oldReferenc+"java",oldStamp,oldStamp+1));
            }
        });
        Thread errorStampThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String reference=asr.getReference();
                System.out.println(Thread.currentThread().getName()+"当前变量值："+reference+"当前版本戳："+asr.getStamp()+"-"
                        +asr.compareAndSet(oldReferenc,oldReferenc+"C",oldStamp,oldStamp+1));
            }
        });
        rightStampThread.start();
        rightStampThread.join();
        errorStampThread.start();
        errorStampThread.join();
        System.out.println(asr.getReference()+"==="+asr.getStamp());

    }
}
