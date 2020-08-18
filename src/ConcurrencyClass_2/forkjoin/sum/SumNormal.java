package ConcurrencyClass_2.forkjoin.sum;

import tools.SleepTools;

/**
 * @author YangYao
 * @date 2020/8/18 9:50
 * @Description 一般求和，单线程
 */
public class SumNormal {
    public static void main(String[] args) {
        int count = 0;
        int[] src = MakeArray.makeArray();

        long start = System.currentTimeMillis();
        for(int i= 0;i<src.length;i++){
//            SleepTools.ms(1);
            count = count + src[i];
        }
        System.out.println("The count is "+count
                +" spend time:"+(System.currentTimeMillis()-start)+"ms");
    }
}
