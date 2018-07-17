package com.qin.springboot.a009mq.activemq;

import org.apache.activemq.ActiveMQConnection;

public class ConstantActiveMq {
    public static final String queue="sample.queue";
    public static final String queue_topic="sample.queue.topic";
    public static final String USERNAME = ActiveMQConnection.DEFAULT_USER;//默认的连接用户名
    public static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;//默认的连接密码
    public static final String BROKERURL = "failover://tcp://192.168.103.93:61616";//默认的连接地址
}
