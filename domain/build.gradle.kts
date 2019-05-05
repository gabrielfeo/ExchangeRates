val kotlin_version: String by project

plugins {
    kotlin("jvm") version "1.3.30"
}

group = "exchangerates"
version = "0.0.1"

repositories {
    mavenLocal()
    jcenter()
}

kotlin.sourceSets["main"].kotlin.srcDirs("src/main")
kotlin.sourceSets["test"].kotlin.srcDirs("src/test")

dependencies {
    // Kotlin standard library
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
}