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
    // implementation platform("org.jetbrains.kotlin:kotlin-bom:$kotlin_bom_version")
    implementation(platform("androidx.compose:compose-bom:2023.06.01"))
    implementation("com.google.accompanist:accompanist-permissions:0.30.1")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.core:core-ktx:1.12.0")

    implementation("io.insert-koin:koin-android:3.4.2")
}
