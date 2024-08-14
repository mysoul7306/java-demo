/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2023. 09. 30
 * Update: 2024. 08. 10
 * All Rights Reserved
 */

rootProject.name = "rokroot-demo"
include("core")
include("jooq-rest-api")

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}
