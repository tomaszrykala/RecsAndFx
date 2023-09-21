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
    implementation(Deps.composeUiWindowSizeClass)

    implementation(Deps.navigationCompose)
    implementation(Deps.lifecycleRuntimeCompose)

    implementation(Deps.koinAndroid)
    implementation(Deps.koinCompose)

    testImplementation(Deps.junit)
    testImplementation(Deps.mockk)
    testImplementation(Deps.coroutinesTest)

    androidTestImplementation(Deps.composeJunit)
}
