package ConcurrentContainer_5.bitwise;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author YangYao
 * @date 2020/8/21 19:05
 * @Description DelayQueue使用和实现
 */
public class ItemVol<T> implements Delayed {
    private long activetime;//到期时间，单位ms

    private T date;

    public ItemVol(long activetime, T date) {
        /**
          *@param [activetime：过期时长, date]
          *return
         */
        this.activetime = TimeUnit.NANOSECONDS.convert(activetime,TimeUnit.MILLISECONDS)
                +System.nanoTime();//将传入的时长转换为超时的时刻
        this.date = date;
    }

    public long getActivetime() {
        return activetime;
    }

    public T getDate() {
        return date;
    }

    //按照剩余时间排序
    @Override
    public int compareTo(Delayed o) {
        //this-o
        long d=getDelay(TimeUnit.NANOSECONDS)-o.getDelay(TimeUnit.NANOSECONDS);
        return d==0?0:((d>0)?1:-1);
    }

    //返回元素剩余时间
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.activetime-System.nanoTime(),TimeUnit.NANOSECONDS);
    }
}
