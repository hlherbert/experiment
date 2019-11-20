package com.hl.javabase.jmx;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;

/**
 * JMX(java management extension) 是一个对象可视化管理监控规范。
 * 用户可以定义MBean对象，然后启动MBeanServer，将MBean注册上去。
 * 之后可以打开jconsole连接到MBeanServer的进程上，通过可视化查看或修改MBean的属性，执行MBean的操作。
 * JMX是为应用程序、设备、系统等植入管理功能的框架
 * JMX可以跨越一系列异构操作系统平台、系统体系结构和网络传输协议，灵活的开发无缝集成的系统、网络和服务管理应用。
 * <p>
 * 用途：  服务端程序部署在生成环境上。启动后不允许停机或者调试。
 * 为了方便查看服务程序的各项运行期指标，可以在服务端程序中定义MBean监控该指标，并启用JMX服务(MBeanServer)，暴露端口PORT。
 * 之后管理员可以通过jconsole，在任意外部PC，远程连接 服务端IP：PORT, 查看服务端程序的VM状态以及各项指标，还可以操作服务端中定义的MBean。
 */
public class JmxDemo {

    private User bean;
    private User bean2;

    public static void main(String[] args) throws MalformedObjectNameException, NullPointerException, InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException, InterruptedException, IOException {
        JmxDemo demo = new JmxDemo();
        demo.registryMBeans();
        demo.startServerLocal();
        //demo.startServerRemote();

        // 步骤：
        // 1. 定义MBean和MBean接口。
        //    标准MBean的定义为 Xxx类以及XxxMBean接口。
        //    扩展MxBean可以将接口名任意指定，但是需要接口上加上注解@MXBean
        //
        // 2. 注册MBean,分2种情况:
        //    (1)如果使用Spring boot框架，会自动注册MBean到MBeanServer
        //    (2)如果未使用Srping boot框架，需要手动注册MBean。(参考registryMBeans)
        //
        // 3. 指定MBeanServer的监听端口，有2种方法
        //   (1) 使用startServerLocal()启动server，
        //     程序在启动时，VM参数需要指定JmxServer端口：
        //    -Dcom.sun.management.jmxremote.port=端口
        //    -Dcom.sun.management.jmxremote.ssl=false
        //    -Dcom.sun.management.jmxremote.authenticate=false
        //    例如： java -jar xxx.jar -Dcom.sun.management.jmxremote.port=1099 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false
        //
        //   (2) 使用startServerRemote()启动，在代码里配置端口和URL。
        //      无需配置VM参数. 可以支持监听多个端口。
        //
        //    推荐用方法(1)
        //
        // 5. 启动后用jconsole, jvisualvm, jmc都可以远程连接 host:port
        //    端口默认是1099
    }

    public void registryMBeans() throws NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException, MalformedObjectNameException {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        // ObjectName定义注册到MBeanServer的MBean名称，在MBeanServer中必须唯一，jmx表示包名，type=xxx表示具体对象名
        ObjectName objectName = new ObjectName("jmx:type=User");
        ObjectName objectName2 = new ObjectName("jmx:type=User2");
        bean = new User();
        bean2 = new User();

        // 注册JmxBean
        server.registerMBean(bean, objectName);
        server.registerMBean(bean2, objectName2);
    }

    // 启动JmxServer,并监听指定本地端口，相当于用代码实现配置JmxServer端口和URL
    public void startServerRemote() throws IOException, InterruptedException {

        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        String oldName = null;

        // 申明9998端口暴露出去做RMI监听
        LocateRegistry.createRegistry(9998);
        // 本jmxserver注册到JNDI上的名称
        JMXServiceURL jmxServiceURL = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9998/jmxrmi");
        JMXConnectorServer jmxConnectorServer = JMXConnectorServerFactory.newJMXConnectorServer(jmxServiceURL, null, server);
        jmxConnectorServer.start();

        System.out.println("jmx started remote on " + jmxServiceURL.toString());
        while (true) {
            if (oldName != bean.getName()) {
                System.out.println(bean.getName());
                oldName = bean.getName();
            }
            // 为了防止程序结束，无限等待
            Thread.sleep(1000);
        }
    }

    // 本地启动JmxServer
    public void startServerLocal() throws NullPointerException, InterruptedException {
        System.out.println("jmx started local");
        while (true) {
            // 为了防止程序结束，无限等待
            Thread.sleep(1000);
        }
    }
}
