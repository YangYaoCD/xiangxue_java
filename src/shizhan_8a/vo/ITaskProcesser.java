package shizhan_8a.vo;

/**
 * @author YangYao
 * @date 2020/8/25 9:56
 * @Description
 */
public interface ITaskProcesser<T,R> {
    TaskResult<R> taskExecute(T data);
}
