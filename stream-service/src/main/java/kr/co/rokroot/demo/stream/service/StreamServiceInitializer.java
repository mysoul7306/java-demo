/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2023. 09. 30
 * Update: 2024. 08. 10
 * All Rights Reserved
 */

package kr.co.rokroot.demo.stream.service;

import kr.co.rokroot.demo.core.events.KafkaConsumerCloseEvent;
import kr.co.rokroot.demo.core.events.KafkaProducerCloseEvent;
import kr.co.rokroot.demo.stream.service.framework.client.KafkaConsumerClient;
import kr.co.rokroot.demo.stream.service.framework.client.KafkaProducerClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.regex.Pattern;

@RequiredArgsConstructor
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class, DataSourceAutoConfiguration.class })
public class StreamServiceInitializer {

    private final KafkaConsumerClient consumerClient;
    private final KafkaProducerClient producerClient;

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(StreamServiceInitializer.class);
        builder.properties();

        final SpringApplication app = builder.build();
        app.setLogStartupInfo(true);
        app.addListeners(new ApplicationPidFileWriter("./rokroot-stream-service.pid"));
        app.run(args);
    }

    @EventListener(ApplicationReadyEvent.class)
    private void ready() {
        this.consumerEvent();
        this.producerEvent();
    }

    @EventListener(KafkaConsumerCloseEvent.class)
    private void consumerEvent() {
        consumerClient.process(Pattern.compile("^*"));
    }

    @EventListener(KafkaProducerCloseEvent.class)
    private void producerEvent() {
        producerClient.process();
    }

}
