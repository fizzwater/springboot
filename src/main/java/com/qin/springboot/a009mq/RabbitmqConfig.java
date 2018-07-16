package com.qin.springboot.a009mq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.amqp.rabbit.support.CorrelationData;
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

        //1默认情况，publisher->server，server不会将publisher的请求的执行情况，返回给publisher。
        // 换句话说，默认，publisher只知道执行了生产消息的动作，不知道server是否已成功存储msg，更不知道msg是否已被consumer消费。
        //2如果，使用confirm模式,publisher->server，server只会告知publisher，是否接收到了请求。
        // publisher只知道server接收到了msg，但不知道msg是否成功存储到queue。
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());

        template.setConfirmCallback(new RabbitTemplate.ConfirmCallback(){
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if (ack) {
                    System.out.println("消息确认成功" );
                } else {
                    //处理丢失的消息（nack）
                    System.out.println("消息确认失败" );
                }
            }
        });


        //当mandatory标志位设置为true时，如果exchange根据自身类型和消息routeKey无法找到一个符合条件的queue，
        // 那么会调用basic.return方法将消息返还给生产者；
        // 当mandatory设为false时，出现上述情形broker会直接将消息扔掉。
        template.setMandatory(true);
        template.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText,
                                        String exchange, String routingKey) {
                /*
                //重新发布
                RepublishMessageRecoverer recoverer = new RepublishMessageRecoverer(errorTemplate,"errorExchange", "errorRoutingKey");
                Throwable cause = new Exception(new Exception("route_fail_and_republish"));
                recoverer.recover(message,cause);*/
                System.out.println("Returned Message code："+replyCode +"msg:" +replyText);
            }
        });

        //当immediate标志位设置为true时，如果exchange在将消息route到queue(s)时发现对应的queue上没有消费者
        // ，那么这条消息不会放入队列中。
        // 当与消息routeKey关联的所有queue(一个或多个)都没有消费者时，
        // 该消息会通过basic.return方法返还给生产者。
        // 在RabbitMQ3.0以后的版本里，去掉了immediate参数支持 ,并建议采用"设置消息TTL"和"DLX"等方式替代。
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
        return BindingBuilder.bind(queueTopic()).to(defaultTopicExchange()).with("#.warning");
    }

    /**
     * 消费者消息的手工确认(全局处理消息，
     * 如果单独处理某个队列)
     * 默认情况下消息消费者是自动 ack （确认）消息的，如果要手动 ack（确认）则需要修改确认模式为 manual
     * @return
     */
    @Bean
    public SimpleMessageListenerContainer messageContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
        container.setQueues(queue());
        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(1);
        container.setConcurrentConsumers(1);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
       /* container.setMessageListener(new ChannelAwareMessageListener() {

            public void onMessage(Message message, com.rabbitmq.client.Channel channel) throws Exception {
                byte[] body = message.getBody();
                System.out.println("消费端接收到消息 : " + new String(body));
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
        });*/
        return container;
    }
}