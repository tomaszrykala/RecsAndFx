plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.tomaszrykala.recsandfx.core.cpp"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }
    buildFeatures {
        compose = true
    }
    externalNativeBuild {
        cmake {
            path = File("./CMakeLists.txt")
            version = "3.22.1"
        }
    }
}

dependencies {
    implementation(Deps.navigationCompose)
}