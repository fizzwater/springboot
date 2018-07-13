package com.qin.springboot.a009mq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/test009")
public class RabbitmqProducer  {

    @Autowired
    private RabbitTemplate rabbitTemplate;


   /* public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        System.out.println(" 消息id:" + correlationData);
        if (ack) {
            System.out.println("消息发送确认成功");
        } else {
            System.out.println("消息发送确认失败:" + cause);

        }
    }*/

    @RequestMapping("sendMq")
    public String send() {
        //执行保存
        String uuid = UUID.randomUUID().toString();

        rabbitTemplate.convertAndSend(ConstantMq.EXCHANGE_NAME,ConstantMq.ROUTING_KEY,"msg test 2:"+uuid);
        rabbitTemplate.convertAndSend(ConstantMq.EXCHANGE_NAME,ConstantMq.ROUTING_KEY2,"msg test 2:"+uuid);
        //rabbitTemplate.convertAndSend(ConstantMq.QUEUE_NAME,ConstantMq.ROUTING_KEY2, "key2:"+uuid);
        return uuid;
    }


}
