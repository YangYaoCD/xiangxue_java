package shizhan_8b;


import shizhan_8b.assist.Consts;
import shizhan_8b.assist.CreatePendingDocs;
import shizhan_8b.assist.SL_QuestionBank;
import shizhan_8b.service.ProduceDocService;
import shizhan_8b.vo.SrcDocVo;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author Mark老师   享学课堂 https://enjoy.ke.qq.com
 * <p>
 * 类说明：rpc服务端，采用生产者消费者模式，生产者消费者还会级联
 */
public class RpcModeWeb {
    //负责生成的线程池
    private static ExecutorService docMakeService= Executors.newFixedThreadPool(Consts.CPU_COUNT*2);
    //负责上传的线程池
    private static ExecutorService docUploadService= Executors.newFixedThreadPool(Consts.CPU_COUNT*2);

    //CompletionService包装线程池，先运行完的先取得结果
    private static CompletionService<String> docCs=new ExecutorCompletionService<>(docMakeService);
    private static CompletionService<String> docUploadCs=new ExecutorCompletionService<>(docUploadService);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("题库开始初始化...........");
        SL_QuestionBank.initBank();
        System.out.println("题库初始化完成。");

        //创建两个待处理文档
        List<SrcDocVo> docList = CreatePendingDocs.makePendingDoc(60);
        long startTotal = System.currentTimeMillis();
        for(SrcDocVo doc:docList){
            docCs.submit(new MakeDocTask(doc));
        }
        for (SrcDocVo doc : docList) {
            docUploadCs.submit(new UploadDocTask(docCs.take().get()));
        }
        for (SrcDocVo doc : docList) {
            System.out.println(docUploadCs.take().get());
        }
        System.out.println("------------共耗时："
                +(System.currentTimeMillis()-startTotal)+"ms-------------");
    }
    //生成文档的任务
    private static class MakeDocTask implements Callable<String> {
        private SrcDocVo pendingDocVo;

        public MakeDocTask(SrcDocVo pendingDocVo) {
            this.pendingDocVo = pendingDocVo;
        }

        @Override
        public String call() throws Exception {
            long start = System.currentTimeMillis();
//            String localName = ProduceDocService.makeDoc(pendingDocVo);
            String localName = ProduceDocService.makeDocAsyn(pendingDocVo);
            System.out.println("文档"+localName+"生成耗时："+(System.currentTimeMillis()-start)+"ms");
            return localName;
        }
    }
    //上传文档的任务
    private static class UploadDocTask implements Callable<String> {
        private String filepath;

        public UploadDocTask(String filepath) {
            this.filepath = filepath;
        }

        @Override
        public String call() throws Exception {
            long start =System.currentTimeMillis();
            String remoteUrl = ProduceDocService.upLoadDoc(filepath);
            System.out.println("已上传至["+remoteUrl+"]耗时："+(System.currentTimeMillis()-start)+"ms");
            return remoteUrl;
        }
    }
}
