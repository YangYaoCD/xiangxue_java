package ThreadBasics_1;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class OnlyMain {
    public static void main(String[] args) {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();/**alt+enter**/
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false); /**ctrl+P方法参数提示显示**/
        for (ThreadInfo threadInfo : threadInfos) {
            System.out.println("{"+threadInfo.getThreadId()+"}"+"\t"+threadInfo.getThreadName());
        }
    }
}
