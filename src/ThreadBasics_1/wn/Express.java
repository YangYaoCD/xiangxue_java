package ThreadBasics_1.wn;

/**
 * @author YangYao
 * @date 2020/8/17 17:11
 * @Description
 *              比较notify()和notifyAll()，看用哪一个
 *                  notify()只唤醒了一个进程，但不指定哪一个，可能唤醒不相干的
 *                  notifyAll()全部唤醒，不管相干不相干
 */
public class Express {
    public final static String CITY = "ShangHai";
    private int km;/*快递运输里程数*/
    private String site;/*快递到达地点*/

    public Express() {
    }

    public Express(int km, String site) {
        this.km = km;
        this.site = site;
    }

    /* 变化公里数，然后通知处于wait状态并需要处理公里数的线程进行业务处理*/
    public synchronized void changeKm(){
        this.km = 101;
        notifyAll();
        //其他的业务代码

    }

    /* 变化地点，然后通知处于wait状态并需要处理地点的线程进行业务处理*/
    public synchronized void changeSite(){
        this.site = "BeiJing";
        notify();
    }

    public synchronized void waitKm(){
        while(this.km<=100) {
            try {
                wait();
                System.out.println("check km thread["+Thread.currentThread().getId()
                        +"] is be notifed.");
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        System.out.println("the km is "+this.km+",I will change db.");
    }

    public synchronized void waitSite(){
        while(CITY.equals(this.site)) {
            try {
                wait();
                System.out.println("check site thread["+Thread.currentThread().getId()
                        +"] is be notifed.");
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        System.out.println("the site is"+this.site+",I will call user.");
    }
}
