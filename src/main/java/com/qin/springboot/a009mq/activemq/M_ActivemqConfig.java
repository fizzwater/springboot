package com.qin.springboot.a009mq.activemq;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;

@Configuration
public class M_ActivemqConfig {

    @Bean(name="activeMQConnectionFactory")
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(ConstantActiveMq.BROKERURL);
        connectionFactory.setUserName(ConstantActiveMq.USERNAME);
        connectionFactory.setPassword(ConstantActiveMq.PASSWORD);
        return connectionFactory;

    }

    @Bean//配置一个消息队列
    public Queue jmsQueue() {
        return new ActiveMQQueue(ConstantActiveMq.queue);
    }


    @Bean(name="jmsTemplateQueue")
    public JmsTemplate jmsTemplateQueue(){
        JmsTemplate jmsTemplate= new JmsTemplate(connectionFactory());
        jmsTemplate.setDefaultDestination(jmsQueue());
        return jmsTemplate;
    }


    @Bean//配置一个消息队列
    public Topic topicQueue() {
        return new ActiveMQTopic(ConstantActiveMq.queue_topic);
    }

    @Bean(name="jmsTemplateTopic")
    public JmsTemplate jmsTemplateTopic(){
        JmsTemplate jmsTemplate= new JmsTemplate(connectionFactory());
        jmsTemplate.setDefaultDestination(topicQueue());
        // 订阅发布模式 -->
        jmsTemplate.setPubSubDomain(true);
        return jmsTemplate;
    }

    /**
     * 有了监听的目标和方法后，监听器还得和 MQ 关联起来，这样才能运作起来。这里的监听器可能不止一个，
     * 如果每个都要和 MQ 建立连接，肯定不太合适。所以需要一个监听容器工厂的概念，即接口JmsListenerContainerFactory，
     * 它会引用上面创建好的与 MQ 的连接工厂，由它来负责接收消息以及将消息分发给指定的监听器。
     * 当然也包括事务管理、资源获取与释放和异常转换等。
     * @return
     */
    @Bean
    public DefaultJmsListenerContainerFactory jmsQueueListenerContainerFactory() {

        DefaultJmsListenerContainerFactory factory =
                new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        //设置连接数
        factory.setConcurrency("3-10");
        //重连间隔时间
        factory.setRecoveryInterval(1000L);
        factory.setPubSubDomain(false);
        return factory;

    }


    @Bean
    public DefaultJmsListenerContainerFactory jmsTopicListenerContainerFactory() {

        DefaultJmsListenerContainerFactory factory =
                new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        //设置连接数
        /**
         * Spring Boot的JmsAnnotationDrivenConfiguration默认使用DefaultJmsListenerContainerFactory
         * 生成DefaultMessageListenerContainer ，而它的内部原理是使用TaskExecutor发起多个线程同时从Queue中拉取消息，
         * 这也就是为什么Spring官方文档里说如果监听的是Topic且concurrency > 1，那么可能会收到重复消息的原因。
         */
        factory.setConcurrency("1");
        //重连间隔时间
        factory.setRecoveryInterval(1000L);
        factory.setPubSubDomain(true);
        return factory;

    }


}
