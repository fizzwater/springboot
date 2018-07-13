package com.qin.springboot.a009mq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = ConstantMq.QUEUE_NAME)
public class RabbitmqProducerConsumerAnotherRk {


    @RabbitHandler
    public void receiver(String msg){
        System.out.println("RabbitmqProducerConsumerBak receiver1:"+msg);
    }



}
