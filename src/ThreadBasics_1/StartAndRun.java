package ThreadBasics_1;

/**
 * @author YangYao
 * @date 2020/8/17 11:23
 * @Description start()和run()方法区别
 */
public class StartAndRun {
    public  static class ThreadRun extends Thread{
        @Override
        public void run() {
            int i=90;
            while (i>0){
                System.out.println("I am "+Thread.currentThread().getName()+" and now the i="+i--);
            }
        }
    }

    public static void main(String[] args) {
        ThreadRun beCalled = new ThreadRun();
        beCalled.setName("BeCalled");
        beCalled.run();
    }
}
