package kg.attractor.bookingsaas.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${spring.rabbitmq.queue.name.book_creation}")
    private String bookCreationQueueName;

    @Value("${spring.rabbitmq.queue.name.book_cancellation}")
    private String bookCancellationQueueName;

    @Value("${spring.rabbitmq.routing.key.book_creation}")
    private String bookCreationRoutingKey;

    @Value("${spring.rabbitmq.routing.key.book_cancellation}")
    private String bookCancellationRoutingKey;

    @Bean
    public Queue bookCreationQueue() {
        return new Queue(bookCreationQueueName);
    }

    @Bean
    public Queue bookCancellationQueue() {
        return new Queue(bookCancellationQueueName);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Binding creationBinding() {
        return BindingBuilder.bind(bookCreationQueue())
                .to(exchange())
                .with(bookCreationRoutingKey);
    }

    @Bean
    public Binding cancellationBinding() {
        return BindingBuilder.bind(bookCancellationQueue())
                .to(exchange())
                .with(bookCancellationRoutingKey);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
