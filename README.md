# xiangxue_java
享学系统课程

#一丶JAVA并发编程
##1.1线程基础、线程之间的共享和协作
###1.1.1基础概念
####1.	什么是进程和线程
进程：程序运行资源分配的最小单位，进程内部有多个线程，会共享这个进程的资源
线程：CPU调度的最小单位，必须依赖进程而存在。
####2.	澄清并行和并发
并行：同一时刻，可以同时处理事情的能力
并发：与单位时间相关，在单位时间内可以处理事情的能力
####3.	高并发编程的意义、好处和注意事项
好处：充分利用cpu的资源、加快用户响应的时间，程序模块化，异步化
问题：
线程共享资源，存在冲突；
容易导致死锁；
启用太多的线程，就有搞垮机器的可能
###1.1.2认识Java里的线程
####1.	Java程序天生就是多线程的，新启线程的方式
类Thread、接口Runnable、接口Callable
单继承、多实现（只能继承一个类，但是可以实现多个接口）
####2.	怎么样才能让Java里的线程安全停止工作呢
线程自然终止：自然执行完或抛出未处理异常
不自然终止：stop()，resume()，suspend()已不建议使用，stop()会导致线程不会正确释放资源，suspend()线程不会释放资源容易导致死锁。java线程是协作式，而非抢占式。可以使用interrupt()、isinterrupted()、static方法isinterrupted()。调用了static方法isinterrupted()后会把中断标志位改为false。
方法内如果抛出InterruptedException，线程的中断标志位会被复位成false，如果确实是需要中断线程，要求我们自己在catch语句块里再次调用interrupt()。
 ![image](https://github.com/YangYaoCD/xiangxue_java/blob/master/src/picture/thread.png)
 
 
##1.2线程的并发工具类



##1.3原子操作CAS



##1.4显示锁和AQS



##1.5并发容器



##1.6线程池和Exector框架
##1.7线程安全
##1.8JMM和底层原理

