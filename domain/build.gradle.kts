val kotlin_version: String by project

plugins {
    kotlin("jvm") version "1.3.72"
}

group = "exchangerates"
version = "0.0.1"

repositories {
    mavenLocal()
    jcenter()
}

dependencies {
    // Kotlin standard library
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    // Unit testing
    testImplementation("org.junit.jupiter:junit-jupiter:5.4.2")
}

kotlin.sourceSets["main"].kotlin.srcDirs("src/main")
kotlin.sourceSets["test"].kotlin.srcDirs("src/test")

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<Test> {
    useJUnitPlatform()
}