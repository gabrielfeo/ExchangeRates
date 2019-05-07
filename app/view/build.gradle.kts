plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {

    compileSdkVersion(28)

    defaultConfig {
        applicationId = "com.gabrielfeo.exchangerates.app.view"
        minSdkVersion(28)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "0.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        setSourceCompatibility(JavaVersion.VERSION_1_8)
        setTargetCompatibility(JavaVersion.VERSION_1_8)
    }

    dataBinding {
        isEnabled = true
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
    }

}


dependencies {
    implementation(kotlin("reflect", "1.3.30"))

    implementation("com.jakewharton.threetenabp:threetenabp:1.2.0")

    val retrofitVersion = "2.5.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:adapter-rxjava2:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-jackson:$retrofitVersion")

    val koinVersion = "1.0.2"
    implementation("org.koin:koin-android:$koinVersion")
    implementation("org.koin:koin-androidx-viewmodel:$koinVersion")
    testImplementation("org.koin:koin-test:$koinVersion")


    implementation("androidx.core:core-ktx:1.0.1")
    implementation("androidx.appcompat:appcompat:1.0.2")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")


    implementation("com.google.android.material:material:1.0.0")

    val lifecycleComponentsVersion = "2.0.0"
    implementation("androidx.lifecycle:lifecycle-runtime:$lifecycleComponentsVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleComponentsVersion")
    implementation("androidx.lifecycle:lifecycle-extensions:$lifecycleComponentsVersion")
//    kapt("androidx.lifecycle:lifecycle-compiler:$lifecycleComponentsVersion")

//    kapt("com.android.databinding:compiler:3.1.4")

    testImplementation("androidx.test:runner:1.1.1")
    testImplementation("androidx.test.espresso:espresso-core:3.1.1")
}