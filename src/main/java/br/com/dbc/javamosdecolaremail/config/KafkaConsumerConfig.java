package br.com.dbc.javamosdecolaremail.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {
    private static final String EARLIEST = "earliest";
    private static final String LATEST = "latest";

    @Value(value = "${spring.kafka.bootstrap-server}")
    private String bootstrapServer;
    @Value(value = "${spring.kafka.properties.security.protocol}")
    private String security;
    @Value(value = "${spring.kafka.properties.sasl.mechanism}")
    private String mechanism;
    @Value(value = "${spring.kafka.properties.enable.idempotence}")
    private boolean idempotence;
    @Value(value = "${spring.kafka.properties.sasl.jaas.config}")
    private String config;
    @Value(value = "${kafka.client-id}")
    private String clienteId;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> listenerContainerFactory1() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.CLIENT_ID_CONFIG, clienteId);
        configProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 10);
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, EARLIEST);
        configProps.put("sasl.mechanism", mechanism);
        configProps.put("sasl.jaas.config", config);
        configProps.put("security.protocol", security);
        configProps.put("enable.idempotence" , idempotence);
        DefaultKafkaConsumerFactory<Object, Object> kafkaConsumerFactory
                = new DefaultKafkaConsumerFactory<>(configProps);

        ConcurrentKafkaListenerContainerFactory<String, String> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaConsumerFactory);
        return factory;
    }
}
