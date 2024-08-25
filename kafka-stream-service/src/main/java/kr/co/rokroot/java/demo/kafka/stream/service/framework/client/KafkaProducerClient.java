/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2023. 09. 30
 * Update: 2024. 08. 10
 * All Rights Reserved
 */

package kr.co.rokroot.java.demo.kafka.stream.service.framework.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import kr.co.rokroot.java.demo.core.events.KafkaProducerCloseEvent;
import kr.co.rokroot.java.demo.core.wrappers.req.KafkaRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.InterruptException;
import org.apache.kafka.common.errors.TimeoutException;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducerClient {

    private final ApplicationEventPublisher publisher;

    private final ObjectMapper om;

    private final Properties props = new Properties();

    @Value("${stream.kafka.host}")
    private String host;

    @Value("${stream.kafka.port}")
    private String port;

    @Value("${stream.kafka.timeout:10}")
    private Long timeout;

    private Producer<String, byte[]> producer;

    @PostConstruct
    private void init() {
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, String.format("%s:%s", host, port));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 9000);
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 5000);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
    }

    @Async(value = "producerThreadPool")
    public void process() {
        try {
            log.info("========= Kafka producer start =========");

            props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "rokroot-demo-" + UUID.randomUUID());
            this.producer = new KafkaProducer<>(props);
            while (true) {
                try {
                    this.producer.initTransactions();
                    break;
                } catch (TimeoutException te) {
                    log.warn(" --- Kafka producer init transaction failed.");
                }
            }
        } catch (InterruptException ie) {
            log.info(" --- Kafka producer shutdown: {}", ie.getMessage());
        } catch (Exception e) {
            log.error("### Kafka producer fail to created: {}", e.getMessage(), e);

            log.info("========= Kafka producer closed =========");
            try {
                Thread.sleep(Duration.ofSeconds(this.timeout).toMillis());
                publisher.publishEvent(new KafkaProducerCloseEvent(this.producer));
            } catch (IllegalArgumentException iae) {
                publisher.publishEvent(new KafkaProducerCloseEvent(new Object()));
            } catch (InterruptedException ie) {
                log.info(" --- Kafka producer shutdown: {}", ie.getMessage());
            }
        }
    }

    public <T extends KafkaRequest> void sendMassage(String topic, T message) {
        this.sendMassage(topic, Collections.singletonList(message));
    }

    public <T extends KafkaRequest> void sendMassage(String topic, List<T> messages) {
        byte[] json;
        for (T msg : messages) {
            try {
                json = om.writeValueAsBytes(msg.getData());
            } catch (Exception e) {
                json = "{}".getBytes(StandardCharsets.UTF_8);
            }

            ProducerRecord<String, byte[]> record = new ProducerRecord<>(
                    topic,
                    null,
                    System.currentTimeMillis(),
                    String.valueOf(msg.hashCode()),
                    json,
                    msg.getHeaders());

            try {
                this.producer.beginTransaction();
                this.producer.send(record).get();
                this.producer.commitTransaction();
            } catch (InterruptedException | InterruptException ie) {
                log.info(" --- Kafka producer shutdown: {}", ie.getMessage());
                if (this.producer != null) {
                    this.producer.flush();
                    this.producer.abortTransaction();
                }
            } catch (Exception e) {
                log.error("### Kafka producer send message(s) failed: {}", e.getMessage());

                if (this.producer != null) {
                    this.producer.flush();
                    this.producer.abortTransaction();
                    this.producer.close(Duration.ofSeconds(this.timeout));
                }

                log.info("========= Kafka producer closed =========");
                publisher.publishEvent(new KafkaProducerCloseEvent(this.producer));
            }
        }
    }
}
