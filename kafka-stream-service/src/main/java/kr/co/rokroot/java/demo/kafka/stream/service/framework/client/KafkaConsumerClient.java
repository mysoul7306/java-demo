/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2023. 09. 30
 * Update: 2024. 08. 10
 * All Rights Reserved
 */

package kr.co.rokroot.java.demo.kafka.stream.service.framework.client;

import kr.co.rokroot.java.demo.core.events.KafkaConsumerCloseEvent;
import kr.co.rokroot.java.demo.kafka.stream.service.framework.factory.KafkaListenerFactory;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.InterruptException;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumerClient {

    private final ApplicationEventPublisher publisher;

    private final Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();

    private final Properties props = new Properties();

    @Value("${stream.kafka.host}")
    private String host;

    @Value("${stream.kafka.port}")
    private String port;

    @Value("${stream.kafka.timeout}")
    private Long timeout;

    private Consumer<String, byte[]> consumer;

    @PostConstruct
    public void init() {
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, String.format("%s:%s", host, port));
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "kafka-stream-service");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 9000);
        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, Integer.MAX_VALUE);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, OffsetResetStrategy.LATEST.toString());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
    }

    @Async(value = "consumerThreadPool")
    public void process(final Pattern pattern) {
        try {
            log.info("========= Kafka consumer start =========");

            this.consumer = new KafkaConsumer<>(props);
            this.consumer.subscribe(pattern, new ConsumerRebalanceListener() {
                @Override
                public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                    log.warn(" --- Lost partitions in re-balance. Committing current offsets: {}", offsets);
                    consumer.commitSync(offsets);
                    offsets.clear();
                }

                @Override
                public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                }
            });

            KafkaListenerFactory listener;
            long offset;
            while (true) {
                ConsumerRecords<String, byte[]> records = this.consumer.poll(Duration.ofMillis(3000L));
                for (ConsumerRecord<String, byte[]> record : records) {
                    TopicPartition partition = new TopicPartition(record.topic(), record.partition());

                    String topic = record.topic();
                    if (topic.contains("order")) {
//                        listener = this.orderListener;
                    } else if (topic.contains("cash")) {
//                        listener = this.cashListener;
                    } else {
                        log.warn(" --- Non match topic and listener topic: {}", topic);
                        continue;
                    }

                    String msg = new String(record.value());
                    log.debug("rule status msg came : {}", msg);
                    try {
//                        listener.listenStatus(msg);
                        offset = record.offset();
                        this.offsets.put(partition, new OffsetAndMetadata(offset, null));
                    } catch (ResponseStatusException rse) {
                        log.error("### Kafka consumer fail to connect API: {}", rse.getMessage());
                    }

                    this.consumer.commitSync();
                }

                if (records.count() > 0) {
                    log.info(" --- {} Message(s) proceed", records.count());
                }
            }
        } catch (Exception e) {
            log.error("### Kafka consumer is dead: {}", e.getMessage());

            if (this.consumer != null) {
                this.consumer.commitSync(this.offsets);
                this.consumer.close(Duration.ofSeconds(this.timeout));
            }

            this.offsets.clear();

            log.info("========= Kafka consumer closed =========");
            try {
                Thread.sleep(Duration.ofSeconds(this.timeout).toMillis());
                publisher.publishEvent(new KafkaConsumerCloseEvent(this.consumer));
            } catch (IllegalArgumentException iae) {
                publisher.publishEvent(new KafkaConsumerCloseEvent(new Object()));
            } catch (InterruptException | InterruptedException ie) {
                log.info(" --- Kafka consumer shutdown: {}", ie.getMessage());
            }
        }
    }
}
