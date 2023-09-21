plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.tomaszrykala.recsandfx.feature.permissions"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(Deps.composeBom))
    implementation(Deps.composeMaterial3)

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("com.google.accompanist:accompanist-permissions:0.30.1")

    implementation("io.insert-koin:koin-android:3.4.2")
}
