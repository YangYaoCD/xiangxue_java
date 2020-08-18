# xiangxue_java
享学系统课程

#一丶JAVA并发编程
##1.1线程基础、线程之间的共享和协作
###1.1.1基础概念
####1.	什么是进程和线程
* 进程：程序运行资源分配的最小单位，进程内部有多个线程，会共享这个进程的资源  
* 线程：CPU调度的最小单位，必须依赖进程而存在。  
####2.	澄清并行和并发
* 并行：同一时刻，可以同时处理事情的能力  
* 并发：与单位时间相关，在单位时间内可以处理事情的能力  
####3.	高并发编程的意义、好处和注意事项
* 好处：充分利用cpu的资源、加快用户响应的时间，程序模块化，异步化  
* 问题：  
    * 线程共享资源，存在冲突；  
    * 容易导致死锁；  
    * 启用太多的线程，就有搞垮机器的可能    
###1.1.2认识Java里的线程
####1.	Java程序天生就是多线程的，新启线程的方式
类Thread、接口Runnable、接口Callable  
单继承、多实现（只能继承一个类，但是可以实现多个接口）  
####2.	怎么样才能让Java里的线程安全停止工作呢
线程自然终止：自然执行完或抛出未处理异常
不自然终止：stop()，resume()，suspend()已不建议使用，stop()会导致线程不会正确释放资源，suspend()线程不会释放资源容易导致死锁。java线程是协作式，而非抢占式。可以使用interrupt()、isinterrupted()、static方法isinterrupted()。调用了static方法isinterrupted()后会把中断标志位改为false。  
interrupt()：不是强制中断线程while(true)，相当于检查中断标志位  
方法内如果抛出InterruptedException，线程的中断标志位会被复位成false，如果确实是需要中断线程，要求我们自己在catch语句块里再次调用interrupt()。  
 ![image](https://github.com/YangYaoCD/xiangxue_java/blob/master/src/picture/thread.png)  
 进一步认识：  
* 1.run()和start()调用的区别，yield()和sleep()的区别。  
* 2.线程优先级（了解，因为有些操作系统不能设置）：1-10，缺省值为5。  
* 3.守护线程与主线程同生共死，finally不一定执行。  
###1.1.3线程间的共享
Synchronized内置锁（区别对象锁，类锁）。  
Volatile关键字，最轻量的同步机制。（无法保证执行的原子性，用处：只有一个线程写，多个线程读，保证可见性）。  
ThreadLocal线程变量。可以理解为是个map，类型 Map<Thread,Integer>  

* A.线程间协作  
轮询：难以保证及时性，资源开销很大。  
等待和通知(对象上的方法)：wait()、notify/notifyAll  
* B.等待和通知的标准范式  
    * 等待方：  
        1. 	获取对象的锁；  
        2.	循环里判断条件是否满足，不满足调用wait方法，  
        3.	条件满足执行业务逻辑  
    * 通知方来说  
        1.	获取对象的锁  
        2.	改变条件  
        3.	通知所有等待在对象的线程
* C.Notify和notifyAll用哪个  
    >用notifyAll()，唤醒想唤醒的和不想唤醒的（基于一个对象拥有相同锁的wait），因为不想信号丢失。   
* D.等待超时模式实现一个连接池  
* E.Join()方法  
    面试点：线程A，执行了线程B的join方法，线程A必须等待B执行完了以后，线程A才能继续自己的工作。    
* F.调用yield() 、sleep()、wait()、notify()等方法对锁有何影响？   
    面试点：  
    * 线程在执行yield()以后，持有的锁是不释放的  
    * sleep()方法被调用以后，持有的锁是不释放的  
    * 调动方法之前，必须要持有锁。调用了wait()方法以后，锁就会被释放，当wait方法返回的时候，线程会重新持有锁  
    * 调动方法之前，必须要持有锁，调用notify()方法本身不会释放锁的    

 
##1.2线程的并发工具类
###1.2.1Fork-join
####1.	什么是分而治之
    规模为N的问题，N<阈值，直接解决，N>阈值，将N分解为K个小规模子问题，子问题互相对立，与原问题形式相同，将子问题的解合并得到原问题的解   
####2.	标准范式
 ![image](https://github.com/YangYaoCD/xiangxue_java/blob/master/src/picture/folkjoin.png)
###1.2.2常用的并发工具类
####1.	CountDownLatch
  * 作用：是一组线程等待其他线程完成工作以后再执行，加强版的join（thread提供的方法）  
  * 运行理解：countDown()一次减一，latch<0，await()方法唤醒。
####2.	CyclicBarrier
  * 作用：让一组线程到达一个屏障，被堵塞，一直到组内最后一个线程到达屏障，屏障开放，所有被堵塞的线程回继续运行。
  * CyclicBarrier(int parties, Runnable barrierAction)，屏蔽开放，barrierAction定义的任务才能执行
    CountDownLatch和CyclicBarrier辨析  
    * 1、countdownlatch放行由第三者控制，CyclicBarrier放行由一组线程本身控制
    * 2、countdownlatch放行条件>=线程数，CyclicBarrier放行条件=线程数
####3.Semaphore
* 控制同时访问某个特定资源的线程数量，用在流量控制。  
* acquire()没有可用permit就会堵塞，等待有permit。  
####4.Exchange
    线程间的数据交换，只允许两个线程间数据交换。
####5.Callable、Future和FutureTask 
* isDone，结束，正常还是异常结束，或者自己取消，返回true；
* isCancelled 任务完成前被取消，返回true；
* cancel（boolean）：
    * 任务还没开始，返回false
    * 任务已经启动，cancel（true），中断正在运行的任务，中断成功，返回true，cancel（false），不会去中断已经运行的任务
    * 任务已经结束，返回false

##1.3原子操作CAS



##1.4显示锁和AQS



##1.5并发容器



##1.6线程池和Exector框架
##1.7线程安全
##1.8JMM和底层原理

