package br.com.dbc.javamosdecolaremail.service;

import br.com.dbc.javamosdecolaremail.dto.EmailUsuarioDTO;
import br.com.dbc.javamosdecolaremail.dto.VendaEmailDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerService {
    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "${kafka.topic}",
            groupId = "${kafka.group-id}",
            topicPartitions = {@TopicPartition(topic = "${kafka.topic}", partitions = {"0", "1"})},
            containerFactory = "listenerContainerFactory1"
    )
    public void sendEmail(@Payload String message, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition) throws JsonProcessingException {
        if(partition == 0) {
            EmailUsuarioDTO emailUsuarioDTO = objectMapper.readValue(message, EmailUsuarioDTO.class);
            emailService.sendEmail(emailUsuarioDTO);
        } else {
            VendaEmailDTO vendaEmailDTO = objectMapper.readValue(message, VendaEmailDTO.class);
            emailService.sendEmail(vendaEmailDTO);
        }
    }
}