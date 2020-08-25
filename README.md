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
###1.3.1什么是原子操作？何如实现原子操作？
    Syn基于阻塞的锁的机制。1、被阻塞的线程优先级很高，2、拿到锁的线程一直不释放怎么办？3、大量竞争，会消耗cpu，同时带来死锁或者其他问题。
    这就是用原子操作的前提（使用锁不方便，笨重）。
####1.CAS原理
（Compare And Swap），指令级别保证这是一个原子操作  
三个运算符：一个内存地址V，一个期望的值A，一个新值B  
基本思路：如果V上的值和期望的值A相等，就给V赋值B，不相等就不做操作。  
循环（死循环，自旋）里不断的进行CAS操作。  
利用了现代处理器都支持CAS指令，循环这个指令，直到成功为止。  
####2.CAS的问题
* a)	ABA问题（加版本号解决）
* b)	开销问题（操作长期不成功，cpu不断循环）
* c)	只能保证一个共享变量的原子操作
###1.3.2原子操作类的使用
####1.	JDK中相关类使用
* 更新基本类型类：AtomicBoolean，AtomicInteger，AtomicLong，AtomicReference
* 更新数组类：AtomicIntegerArray，AtomicLongArray，AtomicReferenceArray
* 更新引用类型：AtomicReference，AtomicMarkableReference，AtomicStampedReference
* 原子更新字段类：AtomicReferenceFieldUpdater，AtomicIntegerFieldUpdater，AtomicLongFieldUpdater
####2.	ABA问题解决
> AtomicMarkableReference：boolean有没有人动过  
AtomicStampedReference：动过几次



##1.4显示锁和AQS
####1.4.1显示锁
#####1.	Lock接口和核心方法
> lock()  
unlock()  
trylock()
#####2.	Lock接口和synchronized的比较
> Synchronized：代码简洁  
Lock：获取锁可以被中断，超时获取锁，尝试获取锁
#####3.	可重入锁ReentrantLock、所谓锁的公平和非公平
    如果在时间上，先对锁进行获取的请求，一定先被满足，这个锁就是公平的，不满足，就是非公平的。
    非公平的效率一般来讲更高
#####4.	ReadWriteLock接口和读写锁ReentrantReadWriteLock，什么情况用读写锁
> ReadWriteLock和Syn关键字，都是排他锁；
> 读写锁：同一时刻允许多个读线程同时访问，但是写线程访问的时候，所有的读和写都被阻塞，最适宜于读多写少的情况。
5.	Condition接口
6.	用Lock和Condition实现等待通知
1.4.2了解LockSupport工具
1.	作用
阻塞一个线程
唤醒一个线程
构建同步组件的基础工具
2.	Park开头的方法
3.	Unpark（Thread thread）方法
1.4.3AbstractQueueSynchronizer深入分析
1.	什么是AQS？学习它的必要性
1)	AQS使用方式和其中的设计模式
继承，模板方法设计模式
2)	了解其中的方法
模板方法（不需要覆盖直接用）：
独占式获取锁：accquire、acquireInterruptibly、tryAcquireNanos
共享式获取锁：acquireShared、acquireSharedInterruptibly、tryAcquireSharedNanos
独占式释放锁：release
共享式释放锁：releaseShared
	需要子类覆盖的流程方法：
		独占式获取：tryAcquire
		独占式释放：tryRelease
		共享式获取：tryAcquireShared
		共享式释放：tryReleaseShared
isHeldExclusively（这个同步器是否处于独占模式）
	同步状态：获取当前的同步状态
		getState：获取当前同步状态
		setState：设置当前同步状态
		compareAndSetState：使用CAS设置状态，保证设置状态原子性
3)	AQS中的数据结构——节点和同步队列
A.	Node里
i.	CANCELLED：线程等待超时或者被中断了，需要从队列中移走
ii.	SIGNAL：后续的节点等待状态，当前节点，通知后面节点运行
iii.	CONDITION：当前节点处在等待队列
iv.	PROPAGATE：共享，表示状态往后面节点传播
v.	0表示初始状态
4)	独占式同步状态获取和释放
#####5.	Condition接口
#####6.	用Lock和Condition实现等待通知
###1.4.2了解LockSupport工具
####1.	作用
>阻塞一个线程  
>唤醒一个线程  
>构建同步组件的基础工具  
####2.	Park开头的方法
####3.	Unpark（Thread thread）方法
###1.4.3AbstractQueueSynchronizer深入分析
####1.	什么是AQS？学习它的必要性
* 1)AQS使用方式和其中的设计模式
继承，模板方法设计模式
* 2)了解其中的方法
    * 模板方法（不需要覆盖直接用）：  
        * 独占式获取锁：accquire、acquireInterruptibly、tryAcquireNanos  
        * 共享式获取锁：acquireShared、acquireSharedInterruptibly、tryAcquireSharedNanos  
        * 独占式释放锁：release  
        * 共享式释放锁：releaseShared  
    * 需要子类覆盖的流程方法：  
        * 独占式获取：tryAcquire
        * 独占式释放：tryRelease
        * 共享式获取：tryAcquireShared
        * 共享式释放：tryReleaseShared
        * isHeldExclusively（这个同步器是否处于独占模式）
    * 同步状态：获取当前的同步状态
        * getState：获取当前同步状态
        * setState：设置当前同步状态
        * compareAndSetState：使用CAS设置状态，保证设置状态原子性
* 3)AQS中的数据结构——节点和同步队列  
    * Node里  
        * i.	CANCELLED：线程等待超时或者被中断了，需要从队列中移走
        * ii.	SIGNAL：后续的节点等待状态，当前节点，通知后面节点运行
        * iii.	CONDITION：当前节点处在等待队列
        * iv.	PROPAGATE：共享，表示状态往后面节点传播
        * v.	0表示初始状态
* 4)独占式同步状态获取和释放
 ![image](https://github.com/YangYaoCD/xiangxue_java/blob/master/src/picture/exclulock.png)
* 5)其他同步状态获取和释放
    * A.	共享式同步状态获取与释放
    * B.	独占式超时同步状态获取
    * C.	再次实战，实现一个奇葩点的三元共享同步工具类
###1.4.4Condition分析
* 1)一个Condition包含一个等待队列（单向链表）
* 2)同步队列与等待队列  
 ![image](https://github.com/YangYaoCD/xiangxue_java/blob/master/src/picture/queue.png)
* 3)节点在队列之间的移动  
  await方法  
 ![image](https://github.com/YangYaoCD/xiangxue_java/blob/master/src/picture/await.png)  
  signal方法  
 ![image](https://github.com/YangYaoCD/xiangxue_java/blob/master/src/picture/signal.png)

##1.5并发容器
###1.5.1ConcurrentHashMap
Hashmap 多线程 put操作会引起死循环，hashmap里的Entry链表产生环形的数据结构。  
putIfAbsent()：没有这个值则放入map，有这个值则返回key本来对应的值。  
####1.	预备知识
* A.Hash
散列，哈希：把任意长度的输入通过一种算法（散列），变换成为固定长度的输出，这个输出值就是散列值。属于压缩映射，容易产生哈希冲突。Hash算法有直接取余法等。  
产生哈希冲突时解决办法：1、开放寻址；2、再散列；3、链地址法（相同hash值的元素用链表串起来）。  
ConcurrentHashMap在发生hash冲突时采用了链地址法。  
md4,md5,sha —— hash算法也属于hash算法，又称摘要算法。  
* B.位运算
用处：权限控制，物品的属性非常多
###1.5.2jdk1.7以前中原理和实现
####1.	ConcurrentHashMap中的数据结构
ConcurrentHashMap是由Segment数组结构和HashEntry数组结构组成。Segment实际继承自可重入锁（ReentrantLock），在ConcurrentHashMap里扮演锁的角色；HashEntry则用于存储键值对数据。一个ConcurrentHashMap里包含一个Segment数组，每个Segment里包含一个HashEntry数组，我们称之为table，每个HashEntry是一个链表结构的元素。  
 ![image](https://github.com/YangYaoCD/xiangxue_java/blob/master/src/picture/concurrenhashmap.jpg)  
####2.	面试常问：
    ConcurrentHashMap实现原理是怎么样的或者问ConcurrentHashMap如何在保证高并发下线程安全的同时实现了性能提升？  
    答：ConcurrentHashMap允许多个修改操作并发进行，其关键在于使用了锁分离技术。它使用了多个锁来控制对hash表的不同部分进行的修改。内部使用段(Segment)来表示这些不同的部分，每个段其实就是一个小的hash table，只要多个修改操作发生在不同的段上，它们就可以并发进行。
####3.	初始化做了什么事？
初始化有三个参数
* initialCapacity：初始容量大小 ，默认16。
* loadFactor, 扩容因子，默认0.75，当一个Segment存储的元素数量大于initialCapacity* loadFactor时，该Segment会进行一次扩容。
* concurrencyLevel 并发度，默认16。并发度可以理解为程序运行时能够同时更新ConccurentHashMap且不产生锁竞争的最大线程数，实际上就是ConcurrentHashMap中的分段锁个数，即Segment[]的数组长度。如果并发度设置的过小，会带来严重的锁竞争问题；如果并发度设置的过大，原本位于同一个Segment内的访问会扩散到不同的Segment中，CPU cache命中率会下降，从而引起程序性能下降。
####4.	在get和put操作中，是如何快速定位元素放在哪个位置的？
对于某个元素而言，一定是放在某个segment元素的某个table元素中的，所以在定位上，
* 定位segment：取得key的hashcode值进行一次再散列（通过Wang/Jenkins算法），拿到再散列值后，以再散列值的高位进行取模得到当前元素在哪个segment上。
* 定位table：同样是取得key的再散列值以后，用再散列值的全部和table的长度进行取模，得到当前元素在table的哪个元素上。
####5.	get（）方法
定位segment和定位table后，依次扫描这个table元素下的的链表，要么找到元素，要么返回null。  
    问：在高并发下的情况下如何保证取得的元素是最新的？  
    答：用于存储键值对数据的HashEntry，在设计上它的成员变量value等都是volatile类型的，这样就保证别的线程对value值的修改，get方法可以马上看到。
####6.	put()方法
	首先定位segment，当这个segment在map初始化后，还为null，由ensureSegment方法负责填充这个segment。
####7.	扩容操作
Segment 不扩容，扩容下面的table数组，每次都是将数组翻倍
####8.	size方法
size的时候进行两次不加锁的统计，两次一致直接返回结果，不一致，重新加锁再次统计
####9.	弱一致性
get方法和containsKey方法都是通过对链表遍历判断是否存在key相同的节点以及获得该节点的value。但由于遍历过程中其他线程可能对链表结构做了调整，因此get和containsKey返回的可能是过时的数据，这一点是ConcurrentHashMap在弱一致性上的体现。  
###1.5.3jdk1.8以后中原理和实现
####1.	与1.7相比的重大变化
* 1、	取消了segment数组，直接用table保存数据，锁的粒度更小，减少并发冲突的概率。
* 2、	存储数据时采用了链表+红黑树的形式，纯链表的形式时间复杂度为O(n)，红黑树则为O（logn），性能提升很大。什么时候链表转红黑树？当key值相等的元素形成的链表中元素个数超过8个的时候。
####2.	主要数据结构和关键变量
Node类存放实际的key和value值。  
sizeCtl：  
* 负数：表示进行初始化或者扩容，-1表示正在初始化，-N，表示有N-1个线程正在进行扩容  
* 正数：0表示还没有被初始化，>0的数，初始化或者是下一次进行扩容的阈值  
TreeNode 用在红黑树，表示树的节点, TreeBin是实际放在table数组中的，代表了这个红黑树的根。
####3.	初始化做了什么事？
    只是给成员变量赋值，put时进行实际数组的填充
####4.	扩容操作
    transfer()方法进行实际的扩容操作，table大小也是翻倍的形式，有一个并发扩容的机制。
####5.	size方法
    估计的大概数量，不是精确数量
####6.	一致性
    弱一致
###1.5.4更多的并发容器
####1.	ConcurrentSkipListMap和ConcurrentSkipListSet
    TreeMap和TreeSet有序的容器，这两种容器的并发版本  
    跳表（SkipList）：以空间换时间，在原链表的基础上形成多层索引，但是某个节点在插入时，是否成为索引，随机决定，所以跳表又称为概率数据结构。
####2.	ConcurrentLinkedQueue
    无界非阻塞队列，底层是个链表，遵循先进先出原则。  
    add,offer将元素插入到尾部，peek（拿头部的数据，但是不移除）和poll（拿头部的数据，但是移除）
####3.	写时复制容器 
    写时复制的容器。通俗的理解是当我们往一个容器添加元素的时候，不直接往当前容器添加，而是先将当前容器进行Copy，复制出一个新的容器，然后新的容器里添加元素，添加完元素之后，再将原容器的引用指向新的容器。这样做的好处是我们可以对容器进行并发的读，而不需要加锁，因为当前容器不会添加任何元素。所以写时复制容器也是一种读写分离的思想，读和写不同的容器。如果读的时候有多个线程正在向容器添加数据，读还是会读到旧的数据，因为写的时候不会锁住旧的，只能保证最终一致性。  
    适用读多写少的并发场景，常见应用：白名单/黑名单， 商品类目的访问和更新场景。
    存在内存占用问题。
###1.5.5阻塞队列
####1.	概念、生产者消费者模式 
* 1)当队列满的时候，插入元素的线程被阻塞，直达队列不满。
* 2)队列为空的时候，获取元素的线程被阻塞，直到队列不空。
####2.	生产者和消费者模式
    生产者就是生产数据的线程，消费者就是消费数据的线程。在多线程开发中，如果生产者处理速度很快，而消费者处理速度很慢，那么生产者就必须等待消费者处理完，才能继续生产数据。同样的道理，如果消费者的处理能力大于生产者，那么消费者就必须等待生产者。为了解决这种生产消费能力不均衡的问题，便有了生产者和消费者模式。生产者和消费者模式是通过一个容器来解决生产者和消费者的强耦合问题。生产者和消费者彼此之间不直接通信，而是通过阻塞队列来进行通信，所以生产者生产完数据之后不用等待消费者处理，直接扔给阻塞队列，消费者不找生产者要数据，而是直接从阻塞队列里取，阻塞队列就相当于一个缓冲区，平衡了生产者和消费者的处理能力。
####3.	常用方法
* 抛出异常：当队列满时，如果再往队列里插入元素，会抛出IllegalStateException（"Queuefull"）异常。当队列空时，从队列里获取元素会抛出NoSuchElementException异常。
* 返回特殊值：当往队列插入元素时，会返回元素是否插入成功，成功返回true。如果是移除方法，则是从队列里取出一个元素，如果没有则返回null。
* 一直阻塞：当阻塞队列满时，如果生产者线程往队列里put元素，队列会一直阻塞生产者线程，直到队列可用或者响应中断退出。当队列空时，如果消费者线程从队列里take元素，队列会阻塞住消费者线程，直到队列不为空。
* 超时退出：当阻塞队列满时，如果生产者线程往队列里插入元素，队列会阻塞生产者线程一段时间，如果超过了指定的时间，生产者线程就会退出。
####4.	常用阻塞队列
* ArrayBlockingQueue：一个由数组结构组成的有界阻塞队列。
    按照先进先出原则，要求设定初始大小
* LinkedBlockingQueue：一个由链表结构组成的有界阻塞队列。
    按照先进先出原则，可以不设定初始大小，Integer.Max_Value
    * ArrayBlockingQueue和LinkedBlockingQueue不同：
        * 锁上面：ArrayBlockingQueue只有一个锁，LinkedBlockingQueue用了两个锁，
        * 实现上：ArrayBlockingQueue直接插入元素，LinkedBlockingQueue需要转换。
* PriorityBlockingQueue：一个支持优先级排序的无界阻塞队列。
    默认情况下，按照自然顺序，要么实现compareTo()方法，指定构造参数Comparator
* DelayQueue：一个使用优先级队列实现的无界阻塞队列。
    支持延时获取的元素的阻塞队列，元素必须要实现Delayed接口。适用场景：实现自己的缓存系统，订单到期，限时支付等等。
* SynchronousQueue：一个不存储元素的阻塞队列。
    每一个put操作都要等待一个take操作
* LinkedTransferQueue：一个由链表结构组成的无界阻塞队列。
    transfer()，必须要消费者消费了以后方法才会返回，tryTransfer()无论消费者是否接收，方法都立即返回。
* LinkedBlockingDeque：一个由链表结构组成的双向阻塞队列。
    可以从队列的头和尾都可以插入和移除元素，实现工作密取，方法名带了First对头部操作，带了last从尾部操作，另外：add=addLast;	remove=removeFirst;	take=takeFirst
####5.	阻塞队列的实现原理
    比如，ArrayBlockingQueue就是基于Lock和Condition实现的。

##1.6线程池
###1.6.1线程池
####1.	什么是线程池？为什么要用线程池？
* A.	降低资源的消耗。降低线程创建和销毁的资源消耗；
* B.	提高响应速度：线程的创建时间为T1，执行时间T2,销毁时间T3，免去T1和T3的时间
* C.	提高线程的可管理性。
####2.	实现一个我们自己的线程池
* A.	线程必须在池子已经创建好了，并且可以保持住，要有容器保存多个线程；
* B.	线程还要能够接受外部的任务，运行这个任务。容器保持这个来不及运行的任务。
####3.	JDK中的线程池和工作机制
* A.	线程池的创建  
ThreadPoolExecutor，jdk所有线程池实现的父类
* B.	各个参数含义  
    * int corePoolSize：线程池中核心线程数，< corePoolSize，就会创建新线程，= corePoolSize，这个任务就会保存到BlockingQueue，如果调用prestartAllCoreThreads（）方法就会一次性的启动corePoolSize个数的线程。
    * int maximumPoolSize, 允许的最大线程数，BlockingQueue也满了，< maximumPoolSize时候就会再次创建新的线程
    * long keepAliveTime, 线程空闲下来后，存活的时间，这个参数只在> corePoolSize才有用
    * TimeUnit unit, 存活时间的单位值
    * BlockingQueue<Runnable> workQueue, 保存任务的阻塞队列
    * ThreadFactory threadFactory, 创建线程的工厂，给新建的线程赋予名字
    * RejectedExecutionHandler handler ：饱和策略
    * AbortPolicy：直接抛出异常，默认；
    * CallerRunsPolicy：用调用者所在的线程来执行任务
    * DiscardOldestPolicy：丢弃阻塞队列里最老的任务，队列里最靠前的任务
    * DiscardPolicy：当前任务直接丢弃  
    实现自己的饱和策略，实现RejectedExecutionHandler接口即可
####4.	提交任务
* execute(Runnable command)  不需要返回
* Future<T> submit(Callable<T> task) 需要返回
####5.	关闭线程池
    shutdown(),shutdownNow();
    shutdownNow():设置线程池的状态，还会尝试停止正在运行或者暂停任务的线程
    shutdown()设置线程池的状态，只会中断所有没有执行任务的线程
####6.	工作机制
###1.6.2合理配置线程池
    根据任务的性质来：计算密集型（CPU），IO密集型，混合型
    计算密集型：加密，大数分解，正则…….， 线程数适当小一点，最大推荐：机器的Cpu核心数+1，为什么+1，防止页缺失，(机器的Cpu核心=Runtime.getRuntime().availableProcessors();)
    IO密集型：读取文件，数据库连接，网络通讯, 线程数适当大一点，机器的Cpu核心数*2,
    混合型：尽量拆分，IO密集型>>计算密集型，拆分意义不大，IO密集型~计算密集型
    队列的选择上，应该使用有界，无界队列可能会导致内存溢出，OOM
###1.6.3预定义的线程池和Executor框架
* A.	FixedThreadPool  
创建固定线程数量的，适用于负载较重的服务器，使用了无界队列
* B.	SingleThreadExecutor  
创建单个线程，需要顺序保证执行任务，不会有多个线程活动，使用了无界队列
* C.	CachedThreadPool  
会根据需要来创建新线程的，执行很多短期异步任务的程序，使用了SynchronousQueue
* D.	WorkStealingPool（JDK7以后）   
基于ForkJoinPool实现
* E.	ScheduledThreadPoolExecutor   
需要定期执行周期任务，Timer不建议使用了。
    * newSingleThreadScheduledExecutor：只包含一个线程，只需要单个线程执行周期任务，保证顺序的执行各个任务
    * newScheduledThreadPool 可以包含多个线程的，线程执行周期任务，适度控制后台线程数量的时候
    * 方法说明：
        * schedule：只执行一次，任务还可以延时执行
        * scheduleAtFixedRate：提交固定时间间隔的任务
        * scheduleWithFixedDelay：提交固定延时间隔执行的任务
        * 两者的区别：
            * scheduleAtFixedRate任务超时：  
规定60s执行一次，有任务执行了80S，下个任务马上开始执行
第一个任务 时长 80s，第二个任务20s，第三个任务 50s
第一个任务第0秒开始，第80S结束；
第二个任务第80s开始，在第100秒结束；
第三个任务第120s秒开始，170秒结束
第四个任务从180s开始
参加代码：ScheduleWorkerTime类：建议在提交给ScheduledThreadPoolExecutor的任务要住catch异常。
###1.6.4Executor框架
###1.6.5了解CompletionService
    先运行完的任务先拿到返回值（与用容器存返回值的区别）。

##1.7线程安全
###1.7.1类的线程安全定义
    如果多线程下使用这个类，不过多线程如何使用和调度这个类，这个类总是表示出正确的行为，这个类就是线程安全的。
类的线程安全表现为：  
* 操作的原子性
* 内存的可见性  
    不做正确的同步，在多个线程之间共享状态的时候，就会出现线程不安全。
###1.7.2怎么才能做到类的线程安全？
* 1.栈封闭  
所有的变量都是在方法内部声明的，这些变量都处于栈封闭状态。
* 2.无状态  
没有任何成员变量的类，就叫无状态的类
* 3.让类不可变  
让状态不可变，两种方式：  
    * A.	加final关键字，对于一个类，所有的成员变量应该是私有的，同样的只要有可能，所有的成员变量应该加上final关键字，但是加上final，要注意如果成员变量又是一个对象时，这个对象所对应的类也要是不可变，才能保证整个类是不可变的。
    * B.	根本就不提供任何可供修改成员变量的地方，同时成员变量也不作为方法的返回值  
AKKA框架
* 4.volatile  
保证类的可见性，最适合一个线程写，多个线程读的情景，
* 5.加锁和CAS
* 6.安全的发布  
类中持有的成员变量，特别是对象的引用，如果这个成员对象不是线程安全的，通过get等方法发布出去，会造成这个成员对象本身持有的数据在多线程下不正确的修改，从而造成整个类线程不安全的问题。
* 7.TheadLocal  
###1.7.3死锁
    资源一定是多于1个，同时小于等于竞争的线程数，资源只有一个，只会产生激烈的竞争。
    死锁的根本成因：获取锁的顺序不一致导致。
    怀疑发生死锁：
    通过jps 查询应用的 id，
    再通过jstack id 查看应用的锁的持有情况
    解决办法：保证加锁的顺序性
* 1.简单的
* 2.动态的
    动态顺序死锁，在实现时按照某种顺序加锁了，但是因为外部调用的问题，导致无法保证加锁顺序而产生的。  
    解决：  
    * 1、通过内在排序，保证加锁的顺序性
    * 2、通过尝试拿锁，也可以。
###1.7.4其他安全问题
* 1.活锁  
尝试拿锁的机制中，发生多个线程之间互相谦让，不断发生拿锁，释放锁的过程。  
解决办法：每个线程休眠随机数，错开拿锁的时间。
* 2.线程饥饿  
###1.7.5性能和思考
####1.影响性能的因素
* 1.上下文切换
* 2.内存同步
####2.	减少锁的竞争
* 缩小锁的范围
* 减少锁的粒度
* 锁分段
* 替换独占锁 
###1.7.6线程安全的单例模式
双重检查锁定
有漏洞
####1.解决之道
* A.	懒汉式  
类初始化模式，也叫延迟占位模式。
* B.	饿汉式  
在JVM中，对类的加载和初始化，有虚拟机保证线程安全。  
枚举

##1.8JMM和底层原理

