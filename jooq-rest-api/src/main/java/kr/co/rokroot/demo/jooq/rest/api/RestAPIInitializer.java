/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2023. 09. 30
 * Update: 2024. 08. 10
 * All Rights Reserved
 */

package kr.co.rokroot.demo.jooq.rest.api;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class, DataSourceAutoConfiguration.class, JooqAutoConfiguration.class })
@RequiredArgsConstructor
public class RestAPIInitializer {

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(RestAPIInitializer.class);
        builder.properties();

        final SpringApplication app = builder.build();
        app.setLogStartupInfo(true);
        app.addListeners(new ApplicationPidFileWriter("./rest-api.pid"));
        app.run(args);
    }

}
