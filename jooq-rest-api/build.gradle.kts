import nu.studer.gradle.jooq.JooqEdition
import org.jooq.meta.jaxb.ForcedType
import org.jooq.meta.jaxb.Logging
import org.jooq.meta.jaxb.Property

plugins {
    id("org.springframework.boot").version("3.3.2")
    id("nu.studer.jooq").version("9.0")
}

springBoot.mainClass.set("kr.co.igloo.rokroot.demo.jooq.rest.api.RestAPIInitializer")
description = "ROK_ROOT Demo jOOQ Rest API module"

var password = ""
if (project.hasProperty("DBPassword")) {
    password = project.property("DBPassword").toString()
}

when (rootProject.extra["ACTIVE_PROFILE"] as String) {
    "dev" -> {
        extra.apply {
            set("MARIADB_HOST", "192.168.1.30")
            set("MARIADB_PORT", "31828")
            set("MARIADB_USER", "rokroot")
            set("MARIADB_PASSWORD", password)
        }
    }

    "prd" -> {
        extra.apply {
            set("MARIADB_HOST", "rokroot.asuscomm.com")
            set("MARIADB_PORT", "33306")
            set("MARIADB_USER", "rokroot")
            set("MARIADB_PASSWORD", password)
        }
    }
}

val libVersions = mapOf(
    "jakarta.persistence.api" to "3.2.0",
    "jakarta.annotation.api" to "3.0.0",
    "spring.boot" to "3.3.2",
    "spring.security" to "6.3.1",
    "springdoc" to "2.5.0",
    "jooq" to "3.19.10",
    "mariadb.client" to "2.7.5",
    "querydsl" to "5.1.0",
    "jsoup" to "1.17.2",
    "jfreechart" to "1.5.5",
    "aspectj" to "1.9.22.1"
)

dependencies {

    implementation(project(":core"))

    annotationProcessor("jakarta.persistence:jakarta.persistence-api:${libVersions["jakarta.persistence.api"]}")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api:${libVersions["jakarta.annotation.api"]}")

    implementation("org.springframework.boot:spring-boot-autoconfigure:${libVersions["spring.boot"]}")
    implementation("org.springframework.boot:spring-boot-starter-web:${libVersions["spring.boot"]}")
    implementation("org.springframework.boot:spring-boot-starter-webflux:${libVersions["spring.boot"]}")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:${libVersions["spring.boot"]}")

    implementation("org.springframework.boot:spring-boot-starter-aop:${libVersions["spring.boot"]}")

    implementation("org.springframework.security:spring-security-web:${libVersions["spring.security"]}")
    implementation("org.springframework.security:spring-security-config:${libVersions["spring.security"]}")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${libVersions["springdoc"]}")

    implementation("org.springframework.boot:spring-boot:${libVersions["spring.boot"]}")
    implementation("org.springframework.boot:spring-boot-starter-jooq:${libVersions["spring.boot"]}")

    implementation("jakarta.persistence:jakarta.persistence-api:${libVersions["jakarta.persistence.api"]}")
    implementation("jakarta.annotation:jakarta.annotation-api:${libVersions["jakarta.annotation.api"]}")

    implementation("org.mariadb.jdbc:mariadb-java-client:${libVersions["mariadb.client"]}")

    implementation("org.jooq:jooq:${libVersions["jooq"]}")
    implementation("org.jooq:jooq-codegen:${libVersions["jooq"]}")
    implementation("org.jooq:jooq-meta:${libVersions["jooq"]}")

    implementation("org.jsoup:jsoup:${libVersions["jsoup"]}")
    implementation("org.jfree:jfreechart:${libVersions["jfreechart"]}")

    testImplementation("org.springframework.boot:spring-boot-starter-test:${libVersions["spring.boot"]}")

    jooqGenerator("org.mariadb.jdbc:mariadb-java-client:${libVersions["mariadb.client"]}")

}

val mariaDBPath = file("src/main/jooq/mariadb")

jooq {
    version.set("${libVersions["jooq"]}")
    edition.set(JooqEdition.OSS)

    configurations {
        create("MariaDB") {
            jooqConfiguration.apply {
                logging = Logging.INFO
                jdbc.apply {
                    driver = "org.mariadb.jdbc.Driver"
                    url = String.format(
                        "jdbc:mariadb://%s:%s/", project.extra["MARIADB_HOST"] as String, project.extra["MARIADB_PORT"] as String
                    )
                    user = project.extra["MARIADB_USER"] as String
                    password = project.extra["MARIADB_PASSWORD"] as String
                    properties = listOf(
                        Property().apply {
                            key = "PAGE_SIZE"
                            value = "3000"
                        }
                    )
                }
                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    database.apply {
                        name = "org.jooq.meta.mariadb.MariaDBDatabase"
                        forcedTypes = listOf(
                            ForcedType().apply {
                                name = "varchar"
                                includeExpression = ".*"
                                includeTypes = "JSONB?"
                            },
                            ForcedType().apply {
                                name = "varchar"
                                includeExpression = ".*"
                                includeTypes = "INET"
                            }
                        )
                        excludes = "information_schema.*"
                    }
                    generate.apply {
                        isDeprecated = false
                        isRecords = false
                        isImmutablePojos = false
                        isFluentSetters = true
                        isJavaTimeTypes = true
                        withFullyQualifiedTypes(".*\\.*")
                    }
                    target.apply {
                        packageName = "kr.co.igloo.rokroot.demo.jooq.rest.api.database.mariadb"
                        directory = mariaDBPath.path
                        encoding = "UTF-8"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}

sourceSets {
    main {
        java {
            srcDirs(listOf("src/main/java", mariaDBPath))
        }
    }
}

tasks {
    compileJava {
        dependsOn("generateMariaDBJooq")
    }
    bootJar {
        archiveFileName = "rokroot-jooq-rest-api.jar"
    }
    processResources {
        filesMatching("**/application.yml") {
            expand(
                "VERSION" to rootProject.extra["VERSION"] as String,

                "MARIADB_HOST" to project.extra["MARIADB_HOST"] as String,
                "MARIADB_PORT" to project.extra["MARIADB_PORT"] as String,
                "MARIADB_USER" to project.extra["MARIADB_USER"] as String,
                "MARIADB_PASSWORD" to project.extra["MARIADB_PASSWORD"] as String,
            )
        }
    }
    build {
        println(String.format("----- !!! ACTIVE PROFILE: %s !!! -----", rootProject.extra["ACTIVE_PROFILE"] as String))
        sourceSets {
            main {
                java {
                    srcDirs(listOf("src/main/java", mariaDBPath))
                }
            }
        }
    }
    sourcesJar {
        dependsOn("generateMariaDBJooq")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}
