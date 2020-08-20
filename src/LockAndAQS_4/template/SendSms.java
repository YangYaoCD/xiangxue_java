package LockAndAQS_4.template;

/**
 * @author YangYao
 * @date 2020/8/19 16:36
 * @Description 模板方法模式的演示
 */
public class SendSms extends SendCustom {
    @Override
    public void to() {
        System.out.println("Mark");
    }

    @Override
    public void from() {
        System.out.println("Bill");
    }

    @Override
    public void content() {
        System.out.println("Hello world!");
    }

    @Override
    public void date() {
        super.date();
    }

    @Override
    public void send() {
        System.out.println("Send SMS");
    }

    @Override
    public void sendMessage() {
        super.sendMessage();
    }

    public static void main(String[] args) {
        SendSms sendSms = new SendSms();
        sendSms.sendMessage();
    }
}
