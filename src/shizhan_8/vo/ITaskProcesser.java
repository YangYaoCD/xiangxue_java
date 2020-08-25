package shizhan_8.vo;

/**
 * @author YangYao
 * @date 2020/8/25 9:56
 * @Description
 */
public interface ITaskProcesser<T,R> {
    TaskResult<R> taskExecute(T data);
}
