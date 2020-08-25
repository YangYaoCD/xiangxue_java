package shizhan_8a;

import ConcurrentContainer_5.bitwise.bq.ItemVo;

import java.util.concurrent.DelayQueue;


/**
 * @author Mark老师   享学课堂 https://enjoy.ke.qq.com
 * <p>
 * 类说明：任务完成后,在一定的时间供查询，之后为释放资源节约内存，需要定期处理过期的任务
 */
public class CheckJobProcesser {
    static {
        Thread thread = new Thread(new FetchJob());
        thread.setDaemon(true);//设置清理的线程为守护线程
        thread.start();
        System.out.println("开启任务过期检查守护线程！");
    }

    private static DelayQueue<ItemVo<String>> queue
            = new DelayQueue<ItemVo<String>>();//存放已完成且等待过期的任务队列

    private CheckJobProcesser() {
    }

    private static class ProcesserHolder {
        private static CheckJobProcesser processer = new CheckJobProcesser();
    }

    public static CheckJobProcesser getInstance() {
        return ProcesserHolder.processer;
    }

    //处理队列中到期任务的线程
    private static class FetchJob implements Runnable {

        @Override
        public void run() {
            while (true){
                try {
                    ItemVo<String> item = queue.take();
                    String jobName = item.getData();
                    PendingJobPool.getMap().remove(jobName);
                    System.out.println(jobName+" is out of date,remove from map!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /*任务完成后，放入队列，经过expireTime时间后，从整个框架中移除*/
    public void putJob(String jobName, long expireTime) {
        ItemVo<String> item = new ItemVo<>(expireTime, jobName);
        queue.offer(item);
        System.out.println(jobName+" 放入了过期检查缓存，过期时长 "+expireTime);
    }


}
