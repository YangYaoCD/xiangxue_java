package ConcurrencyClass_2.forkjoin;


import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author YangYao
 * @date 2020/8/18 11:20
 * @Description 因为汉诺塔问题是个分支问题，我用异步想实现它。刚开始不成功，无法让谁先运行，汉诺塔问题适合串行而不是并行。
 *              第二次尝试使用join方法来实现线程串行，成功
 *              加入了AtomicInteger来记录步骤和步数，我认为记录步骤用AtomicInteger不太好，因为compareAndSet(ai.get(),ai.get()+1)返回一定为true，对于AtomicInteger就没意义。
 *                  步数compareAndSet(a,a+1)就很适合。
 */
public class hanuota {
    static AtomicInteger ai=new AtomicInteger(0);

    private static class HanuotaTask extends RecursiveAction{
        private String A;
        private String B;
        private String C;
        private int num;
        private final int a=ai.get();


        public HanuotaTask(String A,String B,String C, int num) {
            this.A = A;
            this.B = B;
            this.C = C;
            this.num = num;
        }


        @Override
        protected void compute() {
            if (num>0){
                boolean flag=ai.compareAndSet(a,a+1);
                HanuotaTask hanuotaTask = new HanuotaTask(A, C, B, num - 1);//小的到B(A->B)
                invokeAll(hanuotaTask);
                hanuotaTask.join();
                if (flag){
                    System.out.println("第"+num+"个盘子从  "+A+"--->"+C);
                }else {
                    System.out.println("不成功");
                }
                HanuotaTask hanuotaTask2 = new HanuotaTask(B, A, C, num - 1);//再从B到A(B->A)
                invokeAll(hanuotaTask2);
                hanuotaTask2.join();
            }else {
                //System.out.println("完成！");
            }
        }
    }
    public static void main(String [] args){
        try {
            // 用一个 ForkJoinPool 实例调度总任务
            ForkJoinPool pool = new ForkJoinPool();
            HanuotaTask task = new HanuotaTask("A","B","C",3);

            /*与同步的区别*/
            pool.execute(task);//异步调用

            System.out.println("Task is Running......");
            Thread.sleep(1);
            int otherWork = 0;
            for(int i=0;i<100;i++){
                otherWork = otherWork+i;
            }
            System.out.println("Main Thread done sth......,otherWork="+otherWork);
            task.join();//阻塞的方法
            System.out.println("一共多少步："+ai.get());
            System.out.println("Task end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
