val ktor_version: String by project
val kotlin_version: String by project

plugins {
    kotlin("jvm")
}

kotlin.sourceSets["main"].kotlin.srcDirs("src/main")
sourceSets["main"].resources.srcDirs("res/main")

kotlin.sourceSets["test"].kotlin.srcDirs("src/test")
sourceSets["test"].resources.srcDirs("res/test")

dependencies {
    // Application domain model
    api(project(":domain"))

    // Kotlin standard library
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")

    // JSON Serialization
    implementation("io.ktor:ktor-jackson:$ktor_version")

    // Ktor client
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-apache:$ktor_version")

    testImplementation("io.ktor:ktor-client-logging:$ktor_version")
}