plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.tomaszrykala.recsandfx.feature.effect_detail"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(Deps.coreCpp))
    implementation(project(Deps.coreDomain))
    implementation(project(Deps.coreStorage))
    implementation(project(Deps.featureMediaPlayer))

    implementation(platform(Deps.composeBom))
    implementation(Deps.composePreview)
    implementation(Deps.composeMaterial3)
    debugImplementation(Deps.composeUiWindowSizeClass)

    implementation("androidx.navigation:navigation-compose:2.7.3")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")

    implementation("io.insert-koin:koin-android:3.4.2")
    implementation("io.insert-koin:koin-androidx-compose:3.4.3")

    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.13.5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
}
