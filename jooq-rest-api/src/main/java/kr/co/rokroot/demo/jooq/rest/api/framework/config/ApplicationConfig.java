/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2023. 09. 30
 * Update: 2024. 08. 10
 * All Rights Reserved
 */

package kr.co.rokroot.demo.jooq.rest.api.framework.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import kr.co.rokroot.demo.jooq.rest.api.framework.config.jooq.MariaDBJooqConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.tcp.SslProvider;

import javax.net.ssl.SSLException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
@ComponentScan(basePackages = { "kr.co.rokroot.demo.jooq.rest.api.module" },
        includeFilters = @ComponentScan.Filter({ Controller.class, ControllerAdvice.class, Service.class, Repository.class, Component.class }),
        useDefaultFilters = false)
@Import({ MariaDBJooqConfig.class, SwaggerConfig.class })
public class ApplicationConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        om.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        return om;
    }

    @Bean
    public ConnectionProvider httpConnectionProvider() {
        return ConnectionProvider.builder("http-pool")
                .pendingAcquireMaxCount(-1)
                .pendingAcquireTimeout(Duration.ofMillis(0L))
                .maxConnections(30)
                .maxIdleTime(Duration.ofMillis(5000L))
                .build();
    }

    @Bean
    public WebClient webClient() throws SSLException {
        SslContext context = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        SslProvider provider = SslProvider.builder()
                .sslContext(context)
                .build();

        HttpClient client = HttpClient.create()
                .option(ChannelOption.AUTO_CLOSE, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .responseTimeout(Duration.ofMillis(9000))
                .disableRetry(true)
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(3300, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(3300, TimeUnit.MILLISECONDS)));

        return WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024))
                .clientConnector(new ReactorClientHttpConnector(client.secure(provider)))
                .defaultHeaders(header -> {
                    header.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
                    header.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                })
                .build();
    }
}
