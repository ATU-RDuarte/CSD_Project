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
    mainClass.set("org.atu.CarApplication")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")
    testImplementation(libs.kotlin.test.junit)
    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
    testImplementation(libs.kotlinx.coroutines.test)
}
