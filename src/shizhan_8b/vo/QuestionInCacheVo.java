package shizhan_8b.vo;

/**
 * @author YangYao
 * @date 2020/8/26 11:06
 * @Description 题目在缓存中的实体
 */
public class QuestionInCacheVo {
    private final String questionDetail;
    private final String questionSha;

    public String getQuestionDetail() {
        return questionDetail;
    }

    public String getQuestionSha() {
        return questionSha;
    }

    public QuestionInCacheVo(String queestionDetail, String questionSha) {
        this.questionDetail = queestionDetail;
        this.questionSha = questionSha;
    }
}
