/*
 * Author: rok_root
 * E-mail: mysoul7306@outlook.com
 * Create: 2023. 09. 30
 * Update: 2024. 08. 10
 * All Rights Reserved
 */

description = "ROK_ROOT Demo Core Library module"

val libVersions = mapOf(
    "spring.context" to "6.1.11",
    "spring.security" to "6.3.1"
)

dependencies {

    implementation("org.springframework:spring-context:${libVersions["spring.context"]}")
    implementation("org.springframework.security:spring-security-crypto:${libVersions["spring.security"]}")

}
