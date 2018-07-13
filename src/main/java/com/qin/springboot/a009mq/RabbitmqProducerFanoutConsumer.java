package com.qin.springboot.a009mq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = ConstantMq.FANOUT_QUEUE_NAME)
public class RabbitmqProducerFanoutConsumer {


    @RabbitHandler
    public void receiver(String msg){
        System.out.println("fanout receiver:"+msg);
    }



}