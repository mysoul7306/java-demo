rootProject.name = "rokroot-java-demo"
include("core")
include("jooq-rest-api")
include("kafka-stream-service")

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}
