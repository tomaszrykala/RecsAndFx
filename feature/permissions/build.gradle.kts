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

    implementation(Deps.androidCoreKtx)
    implementation(Deps.accompanistPermissions)

    implementation(Deps.koinAndroid)
}
