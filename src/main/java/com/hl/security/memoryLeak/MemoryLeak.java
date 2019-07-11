package com.hl.security.memoryLeak;

/**
 * 练习用JVM工具定位内存泄漏
 * 步骤：
 * 1. 制造内存泄漏（string.intern() 申请内存不释放）
 * 2. 打开jvm工具查看对象数量
 */
public class MemoryLeak {

    private static void makeMemoryLead() throws InterruptedException {
        while (true) {
            String x = String.valueOf(Math.random()).intern();
            Thread.sleep(1000);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        makeMemoryLead();
    }


    /*
    工具汇总：

    【虚拟机统计分析工具】
    jps  -- 查看java进程名，PID
    jinfo pid -- 查看java进程的运行参数和环境变量。 【*】还可调整虚拟机运行参数
    javap -v/-c com.hl.MemoryLeak -- 查看class文件信息，反汇编

    【图形】jconsole -- jvm监视和管理器（查看jvm内存）,堆内存、线程、类、CPU占用率、MBean
    【图形】javavm   -- 加强版jconsole，除了监视进程，可以进行内存、CPU抽样
            【*】看内存和对象数目， 可增量限制 【检查内存泄漏】
            【*】线程DUMP 【检查死锁 Locked ownable synchronizers:	- None】
    【图形】jmc      -- java mission control javavm的升级版，可以对附加的进程进行监控，可以检查线程死锁，还可以设置触发器进行告警通知。
                    可以对进程一段时间进行飞行记录，详细统计内存、热点方法、热点线程、线程争用，锁、IO（套接字、文件）

    jmap -options pid  -- 查看进程加载的dll和占用内存,
    jmap -option server_id@hostname 连接到远程debug server
        options:
        -heap 可统计GC和堆信息
        -histo:live 统计对象数量分布和占用内存
        -clstats  统计classloader，以及它们加载的类数量、内存数
        -finalizerinfo  统计等待析构的对象
       【*】 -dump:file=xx.hprof 导出堆快照。之后可以用jvisualvm加载来查看。

    jstack pid > xx.tdump -- 统计线程信息, 检查死锁（Found 1 deadlock.）

    jstat -option pid interval  -- 统计GC e.g.  jstat -gcutil 13392 1000
        -gc：统计 jdk gc时 heap信息，以使用空间字节数表示
        -gcutil：统计 gc时， heap情况，以使用空间的百分比表示
        -class：统计 class loader行为信息
        -compile：统计编译行为信息
        -gccapacity：统计不同 generations（新生代，老年代，持久代）的 heap容量情况
        -gccause：统计引起 gc的事件
        -gcnew：统计 gc时，新生代的情况
        -gcnewcapacity：统计 gc时，新生代 heap容量
        -gcold：统计 gc时，老年代的情况
        -gcoldcapacity：统计 gc时，老年代 heap容量
        -gcpermcapacity：统计 gc时， permanent区 heap容量

    jstatd -- 启动jvm 监控服务
        它是一个基于 rmi的应用，向远程机器提供本机 jvm应用程序的信息。默认端口 1099。
        $ jstatd -J-Djava.security.policy=my.policy
        my.policy文件需要自己建立，是安全策略文件，因为 jdk对 jvm做了 jaas的安全检测，所以我们必须设置一些策略，使 jstatd被允许作网络操作，内容如下：
        grant codebase " file:$JAVA_HOME/lib/tools.jar " {
            permission java.security.AllPermission;
        };


    jhat xxx.hprof  -- 启动http服务器localhost:7000m, 显示堆dump文件信息。（可以被visualvm替代）
    jdeps dir/class  -- 输出包或类的依赖关系


    jabswitch.exe
    jarsigner.exe
    java-rmi.exe-- Java 远程调用（未用到）

    javac.exe
    java.exe
    javaw.exe
    javaws   -- java web start(浏览器中嵌入java程序）
    javadoc.exe
    javah.exe

    jar.exe
    javapackager.exe
    javafxpackager.exe
    jcmd.exe

    jdb.exe
    jsadebugd.exe

    jjs.exe
    jrunscript.exe
     */
}
