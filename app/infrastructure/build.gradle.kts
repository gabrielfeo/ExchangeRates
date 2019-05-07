plugins {
    kotlin("jvm")
}

kotlin.sourceSets["main"].kotlin.srcDirs("src/main")
kotlin.sourceSets["test"].kotlin.srcDirs("src/test")

dependencies {
    implementation(kotlin("reflect", "1.3.30"))

    implementation("org.threeten:threetenbp:1.3.8")

    val retrofitVersion = "2.5.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-jackson:$retrofitVersion")

    val koinVersion = "1.0.2"
    implementation("org.koin:koin-core:$koinVersion")
    testImplementation("org.koin:koin-test:$koinVersion")
}