plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.tomaszrykala.recsandfx"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.tomaszrykala.recsandfx"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles("proguard-android-optimize.txt", "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(Deps.coreCpp))
    implementation(project(Deps.coreDomain))
    implementation(project(Deps.coreStorage))
    implementation(project(Deps.featureEffectDetail))
    implementation(project(Deps.featureEffectsList))
    implementation(project(Deps.featureMediaPlayer))
    implementation(project(Deps.featurePermissions))

    implementation(platform(Deps.composeBom))
    implementation(Deps.composePreview)
    implementation(Deps.composeMaterial3)
    implementation(Deps.composeUi)
    implementation(Deps.composeUiGraphics)
    debugImplementation(Deps.composeUiTooling)
    debugImplementation(Deps.composeUiTestManifest)
    implementation(Deps.composeUiWindowSizeClass)

    implementation(Deps.activityCompose)
    implementation(Deps.navigationCompose)
    implementation(Deps.lifecycleRuntimeCompose)

    implementation(Deps.accompanistPermissions)

    implementation(Deps.koinAndroid)
    implementation(Deps.koinCompose)

    testImplementation(Deps.junit)
    testImplementation(Deps.mockk)
}
