/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2023. 09. 30
 * Update: 2024. 08. 01
 * All Rights Reserved
 */

package kr.co.rokroot.java.demo.kafka.stream.service.framework.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition
@Configuration
public class SwaggerConfig {

    @Value("${kafka.stream.service.version}")
    private String version;

    @Bean
    public GroupedOpenApi openApi() {
        return GroupedOpenApi.builder()
                .packagesToScan("kr.co.rokroot.java.demo.kafka.stream.service.module")
                .group("v1")
                .pathsToMatch("/rokroot/java/kafka/v1/**")
                .displayName("[ROK_ROOT] jOOQ Demo Kafka API v1")
                .addOpenApiCustomizer(this.customizer())
                .build();
    }

    private OpenApiCustomizer customizer() {
        return openApi -> openApi.setInfo(this.info());
    }

    private Info info() {
        return new Info()
                .title("[ROK_ROOT] jOOQ Demo Kafka API")
                .version(version);
    }
}
