package shizhan_8.vo;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author YangYao
 * @date 2020/8/25 10:15
 * @Description 提交给框架的工作实体类
 */
public class JobInfo<R> {
    //区分唯一的工作
    private final String jobName;
    //工作的任务个数
    private final int jobLength;
    private final ITaskProcesser<?,?> taskProcesser;
    private AtomicInteger successCount;
    private AtomicInteger taskProcesserCount;
    private LinkedBlockingDeque<TaskResult<R>> taskDetailQueue;//拿结果从头拿，放结果从尾巴放
    private final long expireTime;

    public JobInfo(String jobName, int jobLength, ITaskProcesser<?, ?> taskProcesser,
                   LinkedBlockingDeque<TaskResult<R>> taskDetailQueue, long expireTime) {
        this.jobName = jobName;
        this.jobLength = jobLength;
        this.taskProcesser = taskProcesser;
        this.successCount = new AtomicInteger(0);
        this.taskProcesserCount = new AtomicInteger(0);
        this.taskDetailQueue = taskDetailQueue;
        this.expireTime = expireTime;
    }

    public int getSuccessCount() {
        return successCount.get();
    }

    public int getTaskProcesserCount() {
        return taskProcesserCount.get();
    }

    public List<TaskResult<R>> getTaskDetail(){
        List<TaskResult<R>> taskList = new LinkedList<>();
        TaskResult<R> taskResult;
        while ((taskResult=taskDetailQueue.pollFirst())!=null){
            taskList.add(taskResult);
        }
        return taskList;
    }

    //从业务应用角度来讲，保证最终一致性（CAS操作）就可以，不需要加锁
    public void addTaskResult(TaskResult<R> taskResult){
        if (TaskResultType.Success.equals(taskResult.getResultType())){
            successCount.incrementAndGet();
        }
        taskDetailQueue.addLast(taskResult);
        taskProcesserCount.incrementAndGet();
    }
}
