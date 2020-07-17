val kotlinVersion: String by extra { "1.3.72" }
buildscript {
    // The buildscript block executes first, so there must a declaration in scope
    val kotlinVersion: String by extra { "1.3.72" }

    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:4.0.0")
        classpath(kotlin("gradle-plugin", kotlinVersion))
    }
}

// TODO Move to buildSrc
val minSdkVersion: Int by extra { 26 }
val targetSdkVersion: Int by extra { 28 }
val compileSdkVersion: Int by extra { targetSdkVersion }

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

task("clean", type = Delete::class) {
    delete(buildDir)
}
