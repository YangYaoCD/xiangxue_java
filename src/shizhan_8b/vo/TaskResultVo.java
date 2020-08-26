package shizhan_8b.vo;

import java.util.concurrent.Future;

/**
 * @author YangYao
 * @date 2020/8/26 11:25
 * @Description 生成题目，返回处理（两种返回方式1.缓存，2.数据库查）
 */
public class TaskResultVo {
    private final String questionDetail;
    private final Future<QuestionInCacheVo> questionFuture;

    public TaskResultVo(String questionDetail) {
        this.questionDetail = questionDetail;
        this.questionFuture=null;
    }

    public TaskResultVo(Future<QuestionInCacheVo> questionFuture) {
        this.questionDetail=null;
        this.questionFuture = questionFuture;
    }

    public String getQuestionDetail() {
        return questionDetail;
    }

    public Future<QuestionInCacheVo> getQuestionFuture() {
        return questionFuture;
    }
}
