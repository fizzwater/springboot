package com.qin.springboot.a009mq.activemq;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/test009/activemq")
public class M_Producer {
    @Autowired // 也可以注入JmsTemplate，JmsMessagingTemplate对JmsTemplate进行了封装
    @Qualifier("jmsTemplateQueue")
    private JmsTemplate jmsTemplate;

    @Autowired // 也可以注入JmsTemplate，JmsMessagingTemplate对JmsTemplate进行了封装
    @Qualifier("jmsTemplateTopic")
    private JmsTemplate jmsTemplateTopic;

  /*  @Autowired
    private Queue queue;*/


    // 发送消息，destination是发送到的队列，message是待发送的消息
    @RequestMapping("/send")
    public String sendMessage(){
       // public String sendMessage(Destination destination, final String message){
        //jmsTemplate.convertAndSend(destination, message);
        jmsTemplate.convertAndSend(ConstantActiveMq.queue, "hello craig activemq");
        return "1";
    }

    @RequestMapping("/sendTopic")
    public String sendTopic(){
        jmsTemplateTopic.convertAndSend(ConstantActiveMq.queue_topic, " topic hello craig activemq");
        return "2";
    }

}
