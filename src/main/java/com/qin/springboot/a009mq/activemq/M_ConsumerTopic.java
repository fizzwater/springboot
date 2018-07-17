package com.qin.springboot.a009mq.activemq;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class M_ConsumerTopic {
    // 使用JmsListener配置消费者监听的队列，其中text是接收到的消息
    @JmsListener(destination = ConstantActiveMq.queue_topic,containerFactory = "jmsTopicListenerContainerFactory")
    public void receiveQueue(String text) {
        System.out.println("Topic 1 Consumer收到的报文为:"+text);
    }

}
