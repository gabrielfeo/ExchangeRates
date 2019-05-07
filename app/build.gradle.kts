buildscript {

    val kotlinVersion: String by extra { "1.3.11" } // TODO Move to buildSrc

    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:3.3.2")
        classpath(kotlin("gradle-plugin", kotlinVersion))
    }

}

// TODO Move to buildSrc
val minSdkVersion: Int by extra { 23 }
val targetSdkVersion: Int by extra { 28 }
val compileSdkVersion: Int by extra { targetSdkVersion }

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
}

task("clean", type = Delete::class) {
    delete(rootProject.buildDir)
}
