plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.ktor)
    application
}

group = "org.atu"
version = "1.0.0"
var ktorVersion = "3.1.2"
val kotlinVersion = "1.10.2"

application {
    mainClass.set("org.atu.CarApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.logging)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.ktor.client.mock)
    testImplementation(libs.kotlinx.coroutines.test)
}
