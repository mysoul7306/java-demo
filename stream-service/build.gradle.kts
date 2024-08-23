plugins {
    id("org.springframework.boot").version("3.3.2")
}

springBoot.mainClass.set("kr.co.rokroot.demo.stream.service.StreamServiceInitializer")
description = "ROK_ROOT Demo Stream Service module"

when (rootProject.extra["ACTIVE_PROFILE"] as String) {
    "dev" -> {
        extra.apply {
            set("KAFKA_HOST", "192.168.1.30")
            set("KAFKA_PORT", "30900")
        }
    }

    "prd" -> {
        extra.apply {
            set("KAFKA_HOST", "rokroot.asuscomm.com")
            set("KAFKA_PORT", "30900")
        }
    }
}

val libVersions = mapOf(
    "jakarta.persistence.api" to "3.2.0",
    "jakarta.annotation.api" to "3.0.0",
    "spring.boot" to "3.3.2",
    "springdoc" to "2.5.0",
    "apache.kafka" to "3.8.0"
)

dependencies {

    implementation(project(":core"))

    annotationProcessor("jakarta.persistence:jakarta.persistence-api:${libVersions["jakarta.persistence.api"]}")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api:${libVersions["jakarta.annotation.api"]}")

    implementation("org.springframework.boot:spring-boot:${libVersions["spring.boot"]}")
    implementation("org.springframework.boot:spring-boot-autoconfigure:${libVersions["spring.boot"]}")
    implementation("org.springframework.boot:spring-boot-starter-web:${libVersions["spring.boot"]}")

    implementation("org.springframework.boot:spring-boot-starter-aop:${libVersions["spring.boot"]}")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${libVersions["springdoc"]}")

    implementation("jakarta.persistence:jakarta.persistence-api:${libVersions["jakarta.persistence.api"]}")
    implementation("jakarta.annotation:jakarta.annotation-api:${libVersions["jakarta.annotation.api"]}")

    implementation("org.apache.kafka:kafka_2.13:${libVersions["apache.kafka"]}")
    implementation("org.apache.kafka:kafka-clients:${libVersions["apache.kafka"]}")

}

tasks {
    bootJar {
        archiveFileName = "rokroot-stream-service.jar"
    }
    processResources {
        filesMatching("**/application.yml") {
            expand(
                "VERSION" to rootProject.extra["VERSION"] as String,

                "KAFKA_HOST" to project.extra["KAFKA_HOST"] as String,
                "KAFKA_PORT" to project.extra["KAFKA_PORT"] as String,
            )
        }
    }
    build {
        println(String.format("----- !!! ACTIVE PROFILE: %s !!! -----", rootProject.extra["ACTIVE_PROFILE"] as String))
    }
    sourcesJar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}
