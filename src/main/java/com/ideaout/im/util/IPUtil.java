package com.ideaout.im.util;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;

public class IPUtil {
    private static String httpMethod = "http";

    public static String getCurMachineIPAddress() {
        //用 getLocalHost() 方法创建的InetAddress的对象
        String ipAddress = null;
        try {
            InetAddress address = InetAddress.getLocalHost();
            /*System.out.println(address.getHostName());//主机名
            System.out.println(address.getCanonicalHostName());//主机别名
            System.out.println(address.getHostAddress());//获取IP地址*/
            ipAddress = address.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ipAddress;
    }

    public static String getCurMachineIPAddressAndPort() {
        MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
        Set<ObjectName> objectNames = null;
        try {
            objectNames = beanServer.queryNames(new ObjectName("*:type=Connector,*"),
                    Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }
        String port = objectNames.iterator().next().getKeyProperty("port");

        //http://192.168.43.108:8080/pay/notify
        return httpMethod + "://" + getCurMachineIPAddress() + ":" + port;
    }
}
