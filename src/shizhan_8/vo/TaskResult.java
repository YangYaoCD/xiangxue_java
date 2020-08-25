package shizhan_8.vo;

/**
 * @author YangYao
 * @date 2020/8/25 9:57
 * @Description 任务处理结果实体类，用final只提供get方法避免线程安全的问题
 */
public class TaskResult<R> {
    private final TaskResultType resultType;
    private final R returnValue;//方法的业务结果
    private final String reason;//放方法执行失败原因

    public TaskResult(TaskResultType resultType, R returnValue, String reason) {
        this.resultType = resultType;
        this.returnValue = returnValue;
        this.reason = reason;
    }

    public TaskResultType getResultType() {
        return resultType;
    }

    public R getReturnValue() {
        return returnValue;
    }

    public String getReason() {
        return reason;
    }

    public TaskResult(TaskResultType resultType, R returnValue) {
        this.resultType = resultType;
        this.returnValue = returnValue;
        this.reason = "Success";
    }
}
