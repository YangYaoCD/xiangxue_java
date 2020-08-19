package CAS_3;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @author YangYao
 * @date 2020/8/19 10:46
 * @Description AtomicIntegerArray的操作与使用
 */
public class UseAtomicArray {
    static int[] value=new int[]{1,2};

    static AtomicIntegerArray ai=new AtomicIntegerArray(value);
    static AtomicInteger i=new AtomicInteger(0);

    public static void main(String[] args) {
        ai.getAndSet(0,3);
        System.out.println(ai.get(0));
        System.out.println(value[0]);
        for (int j = 0; j < 10; j++) {
            System.out.println("当前的值="+i.get()+i.compareAndSet(i.get(),i.get()+1));
            System.out.println("修改后的值"+i.get());
        }
    }

}
