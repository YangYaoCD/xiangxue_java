package poolAndExector_6.mypool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author YangYao
 * @date 2020/8/22 16:39
 * @Description
 */
public class MyThreadPool {
    // 线程池中默认线程的个数为5
    private static final int WORK_NUM = 5;
    // 队列默认任务个数为100
    private static final int TASK_COUNT = 100;

    private WordThread[] wordThreads;

    private final BlockingQueue<Runnable> taskQueue;
    private final int worker_num;

    public MyThreadPool() {
        this(TASK_COUNT, WORK_NUM);
    }

    public MyThreadPool(int taskCount, int worker_num) {
        if (taskCount <= 0) taskCount = this.TASK_COUNT;
        if (worker_num <= 0) worker_num = this.WORK_NUM;
        this.worker_num = worker_num;
        taskQueue = new ArrayBlockingQueue<>(taskCount);
        wordThreads = new WordThread[worker_num];
        for (int i = 0; i < worker_num; i++) {
            wordThreads[i] = new WordThread();
            wordThreads[i].start();
        }
    }

    public void execute(Runnable task) {
        try {
            taskQueue.put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //销毁线程池，保证所有任务完成的情况下才销毁，否则等待任务完成才销毁
    public void destroy() {
        System.out.println("ready close pool.......");
        for (int i = 0; i < this.worker_num; i++) {
            wordThreads[i].stopWorker();
            wordThreads[i] = null;
        }
        taskQueue.clear();
    }

    private class WordThread extends Thread {
        @Override
        public void run() {
            Runnable r = null;
            try {
                while (!isInterrupted()) {
                    r = taskQueue.take();
                    if (r != null) {
                        System.out.println(Thread.currentThread().getId() + "ready exec：" + r);
                        r.run();
                    }
                    r = null;//help gc
                }
            } catch (Exception e) {
            }
        }

        public void stopWorker() {
            interrupt();
        }
    }
}
