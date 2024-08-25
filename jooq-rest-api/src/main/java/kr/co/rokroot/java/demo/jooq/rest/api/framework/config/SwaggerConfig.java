/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2023. 09. 30
 * Update: 2024. 08. 16
 * All Rights Reserved
 */

package kr.co.rokroot.java.demo.jooq.rest.api.framework.config;

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

    @Value("${rokroot.demo.jooq.verion}")
    private String verion;

    @Bean
    public GroupedOpenApi openApi() {
        return GroupedOpenApi.builder()
                .packagesToScan("kr.co.rokroot.java.demo.jooq.rest.api.module")
                .group("v1")
                .pathsToMatch("/rokroot/java/api/v1/**")
                .displayName("[ROK_ROOT] jOOQ Demo REST API v1")
                .addOpenApiCustomizer(this.customizer())
                .build();
    }

    private OpenApiCustomizer customizer() {
        return openApi -> openApi.setInfo(this.info());
    }

    private Info info() {
        return new Info()
                .title("[ROK_ROOT] jOOQ Demo REST API")
                .version(verion);
    }
}
