package poolAndExector_5;

import tools.SleepTools;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 *@author Mark老师   享学课堂 https://enjoy.ke.qq.com 
 *
 *类说明：线程池的使用
 */
public class UseThreadPool {
    private static AtomicInteger sum=new AtomicInteger(0);
	//工作线程
    static class Worker implements Runnable
    {
        private String taskName;
        private Random r = new Random();

        public Worker(String taskName){
            this.taskName = taskName;
        }

        public String getName() {
            return taskName;
        }

        @Override
        public void run(){
            System.out.println(Thread.currentThread().getName()
            		+" process the task : " + taskName);
            SleepTools.ms(r.nextInt(100)*5);
            for (;;) {
                int num=sum.get();
                if (sum.compareAndSet(num,num+1)){
                    break;
                }
            }
        }
    }
    
    static class CallWorker implements Callable<String>{
    	
        private String taskName;
        private Random r = new Random();

        public CallWorker(String taskName){
            this.taskName = taskName;
        }

        public String getName() {
            return taskName;
        }    	

		@Override
		public String call() throws Exception {
            System.out.println(Thread.currentThread().getName()
            		+" process the task : " + taskName);
            for (;;) {
                int num=sum.get();
                if (sum.compareAndSet(num,num+1)){
                    break;
                }
            }
            return Thread.currentThread().getName()+":"+r.nextInt(100)*5;
		}
    	
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException
    {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 4, 30,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10), new ThreadPoolExecutor.DiscardOldestPolicy());
        for (int i = 0; i < 100; i++) {
            pool.execute(new Worker("worker"+i));
        }
        for (int i = 0; i < 100; i++) {
            Future<String> result = pool.submit(new CallWorker("callworker" + i));
            System.out.println(result.get());
        }
        pool.shutdown();
        System.out.println("计算总数："+sum.get());

    }
}
