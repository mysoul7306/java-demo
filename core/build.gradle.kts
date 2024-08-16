description = "ROK_ROOT Demo Core Library module"

val libVersions = mapOf(
    "spring.context" to "6.1.11",
    "spring.security" to "6.3.1"
)

dependencies {

    implementation("org.springframework:spring-context:${libVersions["spring.context"]}")
    implementation("org.springframework.security:spring-security-crypto:${libVersions["spring.security"]}")

}
