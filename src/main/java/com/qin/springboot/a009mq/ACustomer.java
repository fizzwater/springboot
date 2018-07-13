package com.qin.springboot.a009mq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ACustomer {

    private final String EXCHANGE_NAME="test_ex";
    private final String QUEUE_NAME="test_que";
    private final String ROUTING_KEY="test_rk";


    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接工厂
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
        //声明要关注的队列
        channel.exchangeDeclare(ConstantMq.EXCHANGE_NAME, "direct"); //direct fanout topic
        channel.queueDeclare(ConstantMq.QUEUE_NAME, false, false, true, null);
        channel.queueBind(ConstantMq.QUEUE_NAME, ConstantMq.EXCHANGE_NAME, ConstantMq.ROUTING_KEY);

        System.out.println("Customer Waiting Received messages");
        //DefaultConsumer类实现了Consumer接口，通过传入一个频道，
        // 告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Customer Received '" + message + "'");

            }
        };
        //自动回复队列应答 -- RabbitMQ中的消息确认机制
        channel.basicConsume(ConstantMq.QUEUE_NAME, true, consumer);
    }
}
