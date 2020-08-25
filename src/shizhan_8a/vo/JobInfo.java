package shizhan_8a.vo;

import shizhan_8a.CheckJobProcesser;

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
    private final int taskLength;
    private final ITaskProcesser<?, ?> taskProcesser;
    private AtomicInteger successCount;//成功处理的个数
    private AtomicInteger taskProcesserCount;//已经处理的个数
    private LinkedBlockingDeque<TaskResult<R>> taskDetailQueue;//拿结果从头拿，放结果从尾巴放
    private final long expireTime;

    public JobInfo(String jobName, int jobLength, ITaskProcesser<?, ?> taskProcesser,
                   long expireTime) {
        this.jobName = jobName;
        this.taskLength = jobLength;
        this.taskProcesser = taskProcesser;
        this.successCount = new AtomicInteger(0);
        this.taskProcesserCount = new AtomicInteger(0);
        this.taskDetailQueue = new LinkedBlockingDeque<TaskResult<R>>(jobLength);
        this.expireTime = expireTime;
    }

    public int getSuccessCount() {
        return successCount.get();
    }

    public int getTaskProcesserCount() {
        return taskProcesserCount.get();
    }

    public ITaskProcesser<?, ?> getTaskProcesser() {
        return taskProcesser;
    }

    public List<TaskResult<R>> getTaskDetail() {
        List<TaskResult<R>> taskList = new LinkedList<>();
        TaskResult<R> taskResult;
        while ((taskResult = taskDetailQueue.pollFirst()) != null) {
            taskList.add(taskResult);
        }
        return taskList;
    }

    //从业务应用角度来讲，保证最终一致性（CAS操作）就可以，不需要加锁
    public void addTaskResult(TaskResult<R> taskResult, CheckJobProcesser checkJob) {
        if (TaskResultType.Success.equals(taskResult.getResultType())) {
            successCount.incrementAndGet();
        }
        taskDetailQueue.addLast(taskResult);
        taskProcesserCount.incrementAndGet();
        if (taskProcesserCount.get() == taskLength) {
            checkJob.putJob(jobName, expireTime);
        }
    }

    public int getFailCount() {
        return this.taskProcesserCount.get() - this.successCount.get();
    }

    public String getTotalProcess() {
        return "success[" + successCount.get() + "] / Current[" + taskProcesserCount.get() + "] Total[" + taskLength + "]";
    }
}
