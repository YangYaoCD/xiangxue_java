package ThreadBasics_1;

/**
 * @author YangYao
 * @date 2020/8/17 10:50
 * @Description 安全中断线程(不是强制中断while(true)，相当于检查中断标志位)
 */
public class EndThread {
    private static class UseThread extends Thread{
        public UseThread(String name){
            super(name);
        }

        @Override
        public void run() {
            String threadName=Thread.currentThread().getName();
            while (!isInterrupted()){
                System.out.println(threadName+" is run!");
            }
            System.out.println(threadName+"interruput flag is"+isInterrupted());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread endThread = new UseThread("endTread");
        endThread.start();
        Thread.sleep(20);
        endThread.interrupt();
    }
}
