plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.tomaszrykala.recsandfx.core.storage"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }
}

dependencies {
    implementation(project(Deps.coreCpp))
    implementation(Deps.koinAndroid)

    testImplementation(Deps.junit)
}
