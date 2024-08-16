rootProject.name = "rokroot-demo"
include("core")
include("jooq-rest-api")

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}
