FROM docker.io/eclipse-temurin:21-jdk AS build
WORKDIR /opt/rokroot-demo/

ARG profile
ARG DBPassword

COPY gradlew /opt/rokroot-demo/
COPY gradle /opt/rokroot-demo/gradle/
COPY build.gradle.kts settings.gradle.kts /opt/rokroot-demo/

COPY core/src /opt/rokroot-demo/core/src/
COPY core/build.gradle.kts /opt/rokroot-demo/core/
COPY jooq-rest-api/src /opt/rokroot-demo/jooq-rest-api/src/
COPY jooq-rest-api/build.gradle.kts /opt/rokroot-demo/jooq-rest-api/

RUN chmod +x /opt/rokroot-demo/gradlew
RUN /opt/rokroot-demo/gradlew :jooq-rest-api:build -Pprofile=${profile} -PDBPassword=${DBPassword}

FROM docker.io/eclipse-temurin:21-jre-alpine
WORKDIR /opt/rokroot-demo/jooq-rest-api/

COPY --from=build /opt/rokroot-demo/jooq-rest-api/build/libs/rokroot-jooq-rest-api.jar /opt/rokroot-demo/jooq-rest-api/rokroot-jooq-rest-api.jar

ENTRYPOINT ["java", "-jar", "/opt/rokroot-demo/jooq-rest-api/rokroot-jooq-rest-api.jar", "-XX:+UseZGC", "-XX:+PrintGCDetails", "-XX:+PrintGCDateStamps", "-Xloggc:/opt/rokroot-demo/jooq-rest-api/logs/gc.log", "-XX:+UseGCLogFileRotation", "-XX:NumberOfGCLogFiles=10", "-XX:GCLogFileSize=10M"]
