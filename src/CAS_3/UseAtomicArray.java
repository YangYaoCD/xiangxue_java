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

    private static class plus implements Runnable {
        @Override
        public void run() {
            boolean flag=true;
            while (flag){
                int ori=i.get();
                if(i.compareAndSet(ori,++ori)){
                    flag=false;
                }
            }
            System.out.println(i.get());
        }
    }

    public static void main(String[] args) {
        ai.getAndSet(0,3);
        System.out.println(ai.get(0));
        System.out.println(value[0]);
        plus plus1 = new plus();
        plus plus2 = new plus();
        new Thread(plus1).run();
        new Thread(plus2).run();
    }

}
