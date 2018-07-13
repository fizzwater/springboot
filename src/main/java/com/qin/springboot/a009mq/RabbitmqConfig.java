package com.qin.springboot.a009mq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({"classpath:rabbitmq.properties"})
public class RabbitmqConfig {

    //配置文件取值的第一种方法
    @Value("${mq.rabbit.host}")
    private String mqRabbitHost;

    @Value("${mq.rabbit.port}")
    private int mqRabbitPort;

    @Value("${mq.rabbit.username}")
    private String mqRabbitUserName;

    @Value("${mq.rabbit.password}")
    private String mqRabbitPassword;

    @Value("${mq.rabbit.virtualHost}")
    private String mqRabbitVirtualHost;


    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(this.mqRabbitHost,this.mqRabbitPort);

        connectionFactory.setUsername(this.mqRabbitUserName);
        connectionFactory.setPassword(this.mqRabbitPassword);
        connectionFactory.setVirtualHost(this.mqRabbitVirtualHost);
        connectionFactory.setPublisherConfirms(true);

        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        return template;
    }

    @Bean
    public DirectExchange defaultExchange() {
        return new DirectExchange(ConstantMq.EXCHANGE_NAME,true,false);
        //return DirectExchange.DEFAULT;
    }

    @Bean
    public FanoutExchange defaultFanoutExchange() {
        return new FanoutExchange(ConstantMq.FANOUT_EXCHANGE_NAME);
    }

    @Bean
    public TopicExchange defaultTopicExchange() {
        return new TopicExchange(ConstantMq.TOPIC_EXCHANGE_NAME);
    }

    @Bean
    public Queue queue() {
        return new Queue(ConstantMq.QUEUE_NAME);
    }

    @Bean
    public Queue queue2() {
        return new Queue(ConstantMq.QUEUE_NAME2);
    }

    @Bean
    public Queue queueFanout() {
        return new Queue(ConstantMq.FANOUT_QUEUE_NAME);
    }

    @Bean
    public Queue queueTopic() {
        return new Queue(ConstantMq.TOPIC_QUEUE_NAME);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(defaultExchange()).with(ConstantMq.ROUTING_KEY);
    }

    @Bean
    public Binding binding2() {
        return BindingBuilder.bind(queue2()).to(defaultExchange()).with(ConstantMq.ROUTING_KEY2);
    }

    @Bean
    public Binding bindingFanout() {
        return BindingBuilder.bind(queueFanout()).to(defaultFanoutExchange());
    }

    @Bean
    public Binding bindingTopic() {
        return BindingBuilder.bind(queueTopic()).to(defaultTopicExchange()).with("sms.#.#");
    }

  /*


    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(defaultExchange()).with(ROUTING_KEY);
    }

    @Bean
    public SimpleMessageListenerContainer messageContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
        container.setQueues(queue());
        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(1);
        container.setConcurrentConsumers(1);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setMessageListener(new ChannelAwareMessageListener() {

            public void onMessage(Message message, com.rabbitmq.client.Channel channel) throws Exception {
                byte[] body = message.getBody();
                System.out.println("消费端接收到消息 : " + new String(body));
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
        });
        return container;
    }*/
}