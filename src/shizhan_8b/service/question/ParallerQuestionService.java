package shizhan_8b.service.question;

import shizhan_8b.assist.Consts;
import shizhan_8b.assist.SL_QuestionBank;
import shizhan_8b.vo.QuestionInCacheVo;
import shizhan_8b.vo.QuestionInDBVo;
import shizhan_8b.vo.TaskResultVo;

import java.util.concurrent.*;

/**
 * @author YangYao
 * @date 2020/8/26 12:21
 * @Description 并发的题目处理，缓存用的ConcurrentHashMap还存在一定内存的问题（1.重启服务了数据怎么办，2.一直缓存容易造成内存溢出，考虑使用ConcurrentLinkedHashMap）
 */
public class ParallerQuestionService {
    //已处理题目的缓存
    private static ConcurrentHashMap<Integer, QuestionInCacheVo>
            questionCache = new ConcurrentHashMap<>();
    //正在处理题目的缓存
    private static ConcurrentHashMap<Integer, Future<QuestionInCacheVo>>
            processingQuestionCache = new ConcurrentHashMap<>();

    private static ExecutorService
            makeQuestionService = Executors.newFixedThreadPool(Consts.CPU_COUNT * 2);

    public static TaskResultVo makeQuestion(Integer questionId){
        QuestionInCacheVo qstCacheVo = questionCache.get(questionId);
        if (null==qstCacheVo){
            System.out.println("。。。。。。题目["+questionId+"]在缓存中不存在，准备启动任务！");
            return new TaskResultVo(getQstFuture(questionId));
        }else {
            //比较摘要（看是否修改过内容）
            String questionSha = SL_QuestionBank.getSha(questionId);
            if (questionSha.equals(qstCacheVo.getQuestionSha())){
                System.out.println("。。。。。。题目["+questionId+"]在缓存中已存在，且未改变。");
                return new TaskResultVo(qstCacheVo.getQuestionDetail());
            }else {
                System.out.println("。。。。。。题目["+questionId+"]在缓存中已存在，但是发生了改变，更新缓存！");
                return new TaskResultVo(getQstFuture(questionId));
            }
        }
    }

    public static Future<QuestionInCacheVo> getQstFuture(Integer questionId){
        Future<QuestionInCacheVo> questionFuture = processingQuestionCache.get(questionId);//判断有没有线程在执行这个任务
        try {
            if (questionFuture==null){
                QuestionInDBVo qstDbVo = SL_QuestionBank.getQuetion(questionId);
                QuestionTask questionTask = new QuestionTask(qstDbVo,questionId);
                /*不靠谱，会导致不同线程处理同一业务
                questionFuture=makeQuestionService.submit(questionTask);
                processingQuestionCache.putIfAbsent(questionId,questionFuture);
                */

                FutureTask<QuestionInCacheVo> ft=new FutureTask<>(questionTask);
                questionFuture=processingQuestionCache.putIfAbsent(questionId,ft);
                if (questionFuture==null){
                    questionFuture=ft;
                    makeQuestionService.execute(ft);
                    System.out.println("成功启动了题目["+questionId+"]的计算任务！");

                }else {
                    System.out.println("<<<<<<<<<<<其他线程启动了题目["+questionId+"]的计算任务，本次任务无需开启！");
                }
            }else {
                System.out.println("题目["+questionId+"]已存在计算任务，无需重新生成。");
            }
        } catch (Exception e) {
            processingQuestionCache.remove(questionId);
            e.printStackTrace();
        }
        return questionFuture;
    }

    private static class QuestionTask implements Callable<QuestionInCacheVo>{
        private QuestionInDBVo qstDbVo;
        private Integer questionId;

        public QuestionTask(QuestionInDBVo qstDbVo, Integer questionId) {
            this.qstDbVo = qstDbVo;
            this.questionId = questionId;
        }

        @Override
        public QuestionInCacheVo call() throws Exception {
            try {
                String qstDetail = BaseQuestionProcessor.makeQuestion(questionId,
                        SL_QuestionBank.getQuetion(questionId).getDetail());
                String questionSha=qstDbVo.getSha();
                QuestionInCacheVo qstCache = new QuestionInCacheVo(qstDetail, questionSha);
                questionCache.put(questionId,qstCache);
                return qstCache;
            } finally {
                //不管生成题目的任务正常与否，这个任务都要从正在处理缓存中移除
                processingQuestionCache.remove(questionId);
            }
        }
    }

}
