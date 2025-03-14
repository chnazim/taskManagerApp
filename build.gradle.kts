// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
}

buildscript {
    repositories {
        google() // Make sure Hilt can be resolved
        mavenCentral()
    }
    dependencies {
        // Add the Hilt plugin in buildscript classpath as well for older versions
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.50") // Ensure correct version
    }
}

//allprojects {
//    repositories {
//        google() // Ensure other modules can fetch dependencies from here
//        mavenCentral() // Ensure other modules can fetch dependencies from here
//    }
//}