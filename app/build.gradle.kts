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
    debugImplementation(Deps.composeUiWindowSizeClass)

    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.navigation:navigation-compose:2.7.3")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")

    implementation("com.google.accompanist:accompanist-permissions:0.30.1")

    implementation("io.insert-koin:koin-android:3.4.2")
    implementation("io.insert-koin:koin-androidx-compose:3.4.3")

    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.13.5")
}
