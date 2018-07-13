package com.qin.springboot.a009mq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = ConstantMq.TOPIC_QUEUE_NAME)
public class RabbitmqProducerTopicConsumer {


    @RabbitHandler
    public void receiver(String msg){
        System.out.println("topic receiver:"+msg);
    }



}
