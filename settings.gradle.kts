rootProject.name = "rokroot-demo"
include("core")
include("jooq-rest-api")
include("stream-service")

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}
