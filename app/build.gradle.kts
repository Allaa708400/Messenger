import android.annotation.SuppressLint

plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    id("com.google.gms.google-services")

    id("kotlin-android-extensions")
}
androidExtensions{
    isExperimental = true

}

android {
    namespace = "com.example.messenger"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.messenger"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }


    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }

}





dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)

    testImplementation(libs.junit)
 //   androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.circleimageview)
    implementation(libs.firebase.bom)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.storage.ktx)
    // implementation(libs.firebase.storage)
    //  implementation(libs.firebase.firestore.ktx)
    implementation(libs.glide)
//    implementation(libs.firebase.ui.storage)
    implementation(libs.firebase.core)

implementation("com.android.support:recyclerview-v7:33.0.0")

    implementation(libs.firebase.ui.firestore)

    implementation ("com.xwray:groupie:2.8.0")
    implementation ("com.xwray:groupie-kotlin-android-extensions:2.8.0")

 //   implementation ("com.xwray:groupie:2.8.0")
 //   implementation ("com.xwray:groupie-kotlin-android-extensions:2.8.0")

    //  implementation (libs.groupie)
//    implementation (libs.groupie.databinding)

  //  implementation ("com.github.lisawray.groupie:groupie:1.6.21")
  //  implementation ("com.github.lisawray.groupie:groupie-kotlin-android-extensions:1.6.21")

  //  implementation (libs.groupie)
  //  implementation (libs.groupie.kotlin.android.extensions)


 //   implementation ("com.github.lisawray.groupie:groupie:2.10.1")

  //  implementation (libs.groupie)
    //   implementation (libs.groupie)
 //   implementation (libs.groupie.kotlin.android.extensions)

}


