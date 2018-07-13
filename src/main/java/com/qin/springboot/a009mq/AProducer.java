package com.qin.springboot.a009mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class AProducer {

    public static void main(String[] args) throws IOException, TimeoutException {
        //  声明一个队列        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "Hello RabbitMQ3";

        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置RabbitMQ相关信息
        factory.setHost("192.168.103.93");
        factory.setPort(5672);
        factory.setUsername("qinchao");
        factory.setPassword("123456");
        factory.setVirtualHost("/");

        //创建一个新的连接
        Connection connection = factory.newConnection();
        //创建一个通道
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(ConstantMq.EXCHANGE_NAME, "direct"); //direct fanout topic
        channel.queueDeclare(ConstantMq.QUEUE_NAME, false, false, true, null);
        channel.queueBind(ConstantMq.QUEUE_NAME, ConstantMq.EXCHANGE_NAME, ConstantMq.ROUTING_KEY);

        //发送消息到队列中
        //basicPublish第一个参数为交换机名称、第二个参数为队列映射的路由key、第三个参数为消息的其他属性、第四个参数为发送信息的主体
        channel.basicPublish(ConstantMq.EXCHANGE_NAME, ConstantMq.ROUTING_KEY, null, message.getBytes("UTF-8"));
        System.out.println("Producer Send +'" + message + "'");
        //关闭通道和连接
        channel.close();
        connection.close();
    }
}
