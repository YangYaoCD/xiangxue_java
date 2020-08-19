package CAS_3;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author YangYao
 * @date 2020/8/19 9:41
 * @Description AtomicInteger的操作使用
 */
public class UseAtomicInt {
    static AtomicInteger ai=new AtomicInteger(10);

    public static void main(String[] args) {
        System.out.println(ai.getAndIncrement());
        System.out.println(ai.incrementAndGet());
        System.out.println(ai.get());
    }
}
