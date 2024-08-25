FROM docker.io/eclipse-temurin:21-jdk AS build
WORKDIR /opt/java-demo/

ARG profile
ARG DBPassword

COPY gradlew /opt/java-demo/
COPY gradle /opt/java-demo/gradle/
COPY build.gradle.kts settings.gradle.kts /opt/java-demo/

COPY core/src /opt/java-demo/core/src/
COPY core/build.gradle.kts /opt/java-demo/core/
COPY jooq-rest-api/src /opt/java-demo/jooq-rest-api/src/
COPY jooq-rest-api/build.gradle.kts /opt/java-demo/jooq-rest-api/

RUN chmod +x /opt/java-demo/gradlew
RUN /opt/java-demo/gradlew :jooq-rest-api:build -Pprofile=${profile} -PDBPassword=${DBPassword}

FROM docker.io/eclipse-temurin:21-jre-alpine
WORKDIR /opt/java-demo/jooq-rest-api/

COPY --from=build /opt/java-demo/jooq-rest-api/build/libs/jooq-rest-api.jar \
                  /opt/java-demo/jooq-rest-api/jooq-rest-api.jar

ENTRYPOINT ["java", "-jar", "/opt/java-demo/jooq-rest-api/jooq-rest-api.jar", \
            "-XX:+UseZGC", "-XX:+PrintGCDetails", "-XX:+PrintGCDateStamps", \
            "-Xloggc:/opt/java-demo/jooq-rest-api/logs/gc.log", \
            "-XX:+UseGCLogFileRotation", "-XX:NumberOfGCLogFiles=10", "-XX:GCLogFileSize=10M"]
