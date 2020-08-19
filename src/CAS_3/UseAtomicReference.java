package CAS_3;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author YangYao
 * @date 2020/8/19 9:50
 * @Description AtomicReference的使用，原子包装类和原来那个对象没有联系
 */
public class UseAtomicReference {
    static AtomicReference<UserInfo> userRef = new AtomicReference<UserInfo>();

    public static void main(String[] args) {
        UserInfo user = new UserInfo("Mark", 15);//要修改的实体的实例
        userRef.set(user);

        UserInfo updateUser = new UserInfo("Bill", 17);//要变化的新实例
        userRef.compareAndSet(user, updateUser);//不更新user
        System.out.println(userRef.get().getName());
        System.out.println(userRef.get().getAge());
        System.out.println(user.getName());
        System.out.println(user.getAge());
    }

    //定义一个实体类
    static class UserInfo {
        private String name;
        private int age;
        public UserInfo(String name, int age) {
            this.name = name;
            this.age = age;
        }
        public String getName() {
            return name;
        }
        public int getAge() {
            return age;
        }
    }
}
