package LockAndAQS_4.template;

import java.util.Date;

/**
 * @author YangYao
 * @date 2020/8/19 16:32
 * @Description 发送邮件的基类
 */
public abstract class SendCustom {
    public abstract void to();

    public abstract void from();

    public abstract void content();

    public void date(){
        System.out.println(new Date());
    }

    public abstract void send();

    //框架方法--模板方法
    public void sendMessage(){
        to();
        from();
        content();
        date();
        send();
    }
}
