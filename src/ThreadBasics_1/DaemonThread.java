package ThreadBasics_1;

import java.util.concurrent.ExecutionException;

/**
 * @author YangYao
 * @date 2020/8/17 15:12
 * @Description
 *              守护线程，有点难度，finally不一定执行（这个程序没有执行finally的原因是
 *              程序一直再while循环里没跳出来，如果interrupt了他就可能执行finally）
 */
public class DaemonThread {
    private static class UseThread extends Thread {
        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    System.out.println(Thread.currentThread().getName()
                            + " I am extends Thread.");
                }
                System.out.println(Thread.currentThread().getName()
                        + " interrupt flag is " + isInterrupted());
            } finally {
                System.out.println("...........finally");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException,
            ExecutionException {
        UseThread useThread = new UseThread();
        useThread.setDaemon(true);
        useThread.start();
        Thread.sleep(0);
        useThread.interrupt();
    }
}
