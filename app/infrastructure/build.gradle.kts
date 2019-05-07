plugins {
    kotlin("jvm")
}

kotlin.sourceSets["main"].kotlin.srcDirs("src/main")
kotlin.sourceSets["test"].kotlin.srcDirs("src/test")

dependencies {
    api(project(":domain"))

    implementation(kotlin("reflect", "1.3.30"))
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.2.1")

    api("org.threeten:threetenbp:1.3.8")

    implementation("org.joda:joda-money:1.0.1")

    val retrofitVersion = "2.5.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-jackson:$retrofitVersion")

    implementation("com.squareup.okhttp3:logging-interceptor:3.14.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")

    val koinVersion = "1.0.2"
    api("org.koin:koin-core:$koinVersion")
    testImplementation("org.koin:koin-test:$koinVersion")

    testImplementation("org.junit.jupiter:junit-jupiter:5.4.2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}