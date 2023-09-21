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
    implementation(project(":core:cpp"))
    implementation("io.insert-koin:koin-android:3.4.2")

    testImplementation("junit:junit:4.13.2")
}