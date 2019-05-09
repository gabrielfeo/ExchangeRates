val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm")
    id("war")
    id("com.google.cloud.tools.appengine")
}

appengine.deploy {
    projectId = "gabrielfeo-exchange-rates"
    version = "production"
}

kotlin.sourceSets["main"].kotlin.srcDirs("src/main")
sourceSets["main"].resources.srcDirs("res/main")

kotlin.sourceSets["test"].kotlin.srcDirs("src/test")
sourceSets["test"].resources.srcDirs("res/test")

dependencies {
    // Application domain model
    api(project(":api:infrastructure"))

    // Kotlin standard library
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")

    // Ktor server
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-servlet:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")

    // JSON Serialization
    implementation("io.ktor:ktor-jackson:$ktor_version")

    // Logging
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // Ktor client
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-apache:$ktor_version")

    // Google Cloud Platform App Engine
    providedCompile("com.google.appengine:appengine:1.9.74")

    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
}