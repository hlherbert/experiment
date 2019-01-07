package com.hl.javabase.jmx;

import javax.management.*;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class JmxDemo {


    // 远程启动
    public static void startRemote() throws IOException, MalformedObjectNameException,
            NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException, InterruptedException {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = new ObjectName("jmx:type=User");
        ObjectName objectName2 = new ObjectName("jmx:type=User2");
        User bean = new User();
        User bean2 = new User();

        // 注册到本地
        server.registerMBean(bean, objectName);
        server.registerMBean(bean2, objectName2);
        String oldName = null;

        Registry registry = LocateRegistry.createRegistry(1099);
        JMXServiceURL jmxServiceURL = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi");
        JMXConnectorServer jmxConnectorServer = JMXConnectorServerFactory.newJMXConnectorServer(jmxServiceURL, null, server);
        jmxConnectorServer.start();

        System.out.println("jmx started on localhost:1099!!!");
        while (true) {
            if (oldName != bean.getName()) {
                System.out.println(bean.getName());
                oldName = bean.getName();
            }
            Thread.sleep(1000);
        }
    }

    // 本地启动
    public static void startServerOnLocal()  throws MalformedObjectNameException, NullPointerException, InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException, InterruptedException {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = new ObjectName("jmx:type=User");
        ObjectName objectName2 = new ObjectName("jmx:type=User2");
        User bean = new User();
        User bean2 = new User();

        // 注册到本地
        server.registerMBean(bean, objectName);
        server.registerMBean(bean2, objectName2);
        String oldName = null;

        System.out.println("jmx started!!!");
        while (true) {
            if (oldName != bean.getName()) {
                System.out.println(bean.getName());
                oldName = bean.getName();
            }
            Thread.sleep(1000);
        }
    }
    // JMX(java management extension) 是一个对象可视化管理监控规范。
    // 用户可以定义MBean对象，然后启动MBeanServer，将MBean注册上去。
    // 之后可以打开jconsole连接到MBeanServer的进程上，通过可视化查看或修改MBean的属性，执行MBean的操作。
    //
    // JMX是为应用程序、设备、系统等植入管理功能的框架
    // JMX可以跨越一系列异构操作系统平台、系统体系结构和网络传输协议，灵活的开发无缝集成的系统、网络和服务管理应用。
    public static void main(String[] args) throws MalformedObjectNameException, NullPointerException, InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException, InterruptedException, IOException {
        //startServerOnLocal();
        startRemote();
    }
}
