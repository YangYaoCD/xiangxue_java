package ConcurrencyClass_2.tools;

import tools.SleepTools;

import java.util.concurrent.CountDownLatch;

/**
 * @author YangYao
 * @date 2020/8/18 16:33
 * @Description CountDownLatch的使用，5个初始化线程，6个扣除点
 *                  扣除完毕后，主线程和业务线程才能继续工作
 *                              countDown()一次减一，latch<=0，await()方法唤醒
 */
public class UseCountDown {
    static CountDownLatch latch = new CountDownLatch(6);

    //初始化线程(只有一步，有4个)
    private static class InitThread implements Runnable{

        @Override
        public void run() {
            System.out.println("Thread_"+Thread.currentThread().getId()
                    +" ready init work......");
            latch.countDown();//初始化线程完成工作了，countDown方法只扣减一次；
            for(int i =0;i<2;i++) {
                System.out.println("Thread_"+Thread.currentThread().getId()
                        +" ........continue do its work");
            }
        }
    }

    //业务线程
    private static class BusiThread implements Runnable{

        @Override
        public void run() {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(int i =0;i<3;i++) {
                System.out.println("BusiThread_"+Thread.currentThread().getId()
                        +" do business-----");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        //单独的初始化线程,初始化分为2步，需要扣减两次
        new Thread(new Runnable() {
            @Override
            public void run() {
                SleepTools.ms(1);
                System.out.println("Thread_"+Thread.currentThread().getId()
                        +" ready init work step 1st......");
                latch.countDown();//每完成一步初始化工作，扣减一次
                System.out.println("begin step 2nd.......");
                SleepTools.ms(1);
                System.out.println("Thread_"+Thread.currentThread().getId()
                        +" ready init work step 2nd......");
                latch.countDown();//每完成一步初始化工作，扣减一次
            }
        }).start();
        new Thread(new BusiThread()).start();
        for(int i=0;i<=3;i++){
            Thread thread = new Thread(new InitThread());
            thread.start();
        }

        latch.await();
        System.out.println("Main do ites work........");
    }
}
