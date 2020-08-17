package ThreadBasics_1;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class NewThread {
    /*实现Runnable接口*/
    private static class UseRun implements Runnable{
        @Override
        public void run() {
            System.out.println("I am implements Runnable");
        }
    }

    /*实现Runnable接口*/
    private static class UseCall implements Callable{
        @Override
        public Object call() throws Exception {
            System.out.println("I am implements Callable");
            return "CallResult";
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        UseRun useRun = new UseRun();
        new Thread(useRun).start();
        UseCall useCall = new UseCall();
        FutureTask futureTask = new FutureTask<>(useCall);
        new Thread(futureTask).start();
        System.out.println(futureTask.get());
    }
}
