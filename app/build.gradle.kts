plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    namespace = "com.wstxda.clippy"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.wstxda.clippy"
        minSdk = 24
        targetSdk = 35
        versionCode = 100
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
    }

    kotlinOptions {
        jvmTarget = "18"
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
}