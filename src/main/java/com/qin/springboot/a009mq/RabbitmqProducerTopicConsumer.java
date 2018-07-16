package com.qin.springboot.a009mq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RabbitListener(queues = ConstantMq.QUEUE_NAME)
public class RabbitmqProducerTopicConsumer {


   /*
   //我们使用SpringBoot注解,去实现，但是控制权不完全掌握，
   //当进行大规模项目时候，不太建议使用  ,建议使用 SimpleMessageListenerContainer
   @RabbitHandler
    public void receiver(String msg){
        System.out.println("topic receiver:"+msg);
    }*/

   /* @RabbitHandler
    public void processMessage2(String message,Channel channel,@Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        System.out.println(message);
        try {
            channel.basicAck(tag,false);            // 确认消息
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    @RabbitHandler
    public void processMessage2(String message, Channel channel,@Headers Map<String,Object> map) {
        System.out.println(message);
        if (map.get("error")!= null){
            System.out.println("错误的消息");
            try {
                channel.basicNack((Long)map.get(AmqpHeaders.DELIVERY_TAG),false,true);      //否认消息
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            channel.basicAck((Long)map.get(AmqpHeaders.DELIVERY_TAG),false);            //确认消息
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    }
