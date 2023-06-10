buildscript {
    repositories {
        google()
        mavenCentral()
      //  maven { url ("https://jitpack.io") }  // <--
        jcenter() // Warning: this repository is going to shut down soon
    }
    dependencies {
        classpath(libs.google.services)


    }
}

plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false



}

/*

buildscript {
    repositories {
        maven {
            url "https://maven.google.com"
        }

        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.0.0'
    }
}

 */