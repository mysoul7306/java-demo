FROM docker.io/eclipse-temurin:21-jdk AS build
WORKDIR /opt/java-demo/

ARG profile

COPY gradlew /opt/java-demo/
COPY gradle /opt/java-demo/gradle/
COPY build.gradle.kts settings.gradle.kts /opt/java-demo/

COPY core/src /opt/java-demo/core/src/
COPY core/build.gradle.kts /opt/java-demo/core/
COPY kafka-stream-service/src /opt/java-demo/kafka-stream-service/src/
COPY kafka-stream-service/build.gradle.kts /opt/java-demo/kafka-stream-service/

RUN chmod +x /opt/java-demo/gradlew
RUN /opt/java-demo/gradlew :kafka-stream-service:build -Pprofile=${profile}

FROM docker.io/eclipse-temurin:21-jre-alpine
WORKDIR /opt/java-demo/kafka-stream-service/

COPY --from=build /opt/java-demo/kafka-stream-service/build/libs/kafka-stream-service.jar \
                  /opt/java-demo/kafka-stream-service/kafka-stream-service.jar

ENTRYPOINT ["java", "-jar", "/opt/java-demo/kafka-stream-service/kafka-stream-service.jar", \
            "-XX:+UseZGC", "-XX:+PrintGCDetails", "-XX:+PrintGCDateStamps", \
            "-Xloggc:/opt/java-demo/kafka-stream-service/logs/gc.log", \
            "-XX:+UseGCLogFileRotation", "-XX:NumberOfGCLogFiles=10", "-XX:GCLogFileSize=10M"]
