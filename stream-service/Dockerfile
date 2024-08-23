FROM docker.io/eclipse-temurin:21-jdk AS build
WORKDIR /opt/rokroot-demo/

ARG profile

COPY gradlew /opt/rokroot-demo/
COPY gradle /opt/rokroot-demo/gradle/
COPY build.gradle.kts settings.gradle.kts /opt/rokroot-demo/

COPY core/src /opt/rokroot-demo/core/src/
COPY core/build.gradle.kts /opt/rokroot-demo/core/
COPY stream-service/src /opt/rokroot-demo/stream-service/src/
COPY stream-service/build.gradle.kts /opt/rokroot-demo/stream-service/

RUN chmod +x /opt/rokroot-demo/gradlew
RUN /opt/rokroot-demo/gradlew :stream-service:build -Pprofile=${profile}

FROM docker.io/eclipse-temurin:21-jre-alpine
WORKDIR /opt/rokroot-demo/stream-service/

COPY --from=build /opt/rokroot-demo/stream-service/build/libs/rokroot-stream-service.jar /opt/rokroot-demo/stream-service/rokroot-stream-service.jar

ENTRYPOINT ["java", "-jar", "/opt/rokroot-demo/stream-service/rokroot-stream-service.jar", "-XX:+UseZGC", "-XX:+PrintGCDetails", "-XX:+PrintGCDateStamps", "-Xloggc:/opt/rokroot-demo/stream-service/logs/gc.log", "-XX:+UseGCLogFileRotation", "-XX:NumberOfGCLogFiles=10", "-XX:GCLogFileSize=10M"]
