subprojects {
    repositories {
        mavenCentral()
        jcenter()
    }
}

buildscript {
    repositories {
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath("com.google.cloud.tools:appengine-gradle-plugin:2.0.0")
    }
}