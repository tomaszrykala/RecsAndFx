object Deps {
    private const val compose_bom_version = "2023.06.01"
    private const val koin_android_version = "3.4.2"
    private const val koin_compose_version = "3.4.3"
    private const val compose_activity_version = "1.7.2"
    private const val compose_lifecycle_version = "2.6.2"
    private const val compose_navigation_version = "2.7.3"
    private const val android_ktx_core_version = "1.12.0"
    private const val accompanist_perms_version = "0.30.1"
    private const val junit_version = "4.13.2"
    private const val mockk_version = "1.13.5"
    private const val coroutines_test_version = "1.7.3"

    // App Modules
    val coreCpp = ":core:cpp"
    val coreDomain = ":core:domain"
    val coreStorage = ":core:storage"
    val featureEffectDetail = ":feature:effect_detail"
    val featureEffectsList = ":feature:effects_list"
    val featureMediaPlayer = ":feature:media_player"
    val featurePermissions = ":feature:permissions"

    // Compose
    val composeBom = "androidx.compose:compose-bom:$compose_bom_version"
    val composePreview = "androidx.compose.ui:ui-tooling-preview"
    val composeMaterial3 = "androidx.compose.material3:material3"
    val composeUi = "androidx.compose.ui:ui"
    val composeUiGraphics = "androidx.compose.ui:ui-graphics"
    val composeUiTooling = "androidx.compose.ui:ui-tooling"
    val composeUiTestManifest = "androidx.compose.ui:ui-test-manifest"
    val composeUiWindowSizeClass = "androidx.compose.material3:material3-window-size-class"

    // Compose support
    val activityCompose = "androidx.activity:activity-compose:$compose_activity_version"
    val navigationCompose = "androidx.navigation:navigation-compose:$compose_navigation_version"
    val lifecycleRuntimeCompose = "androidx.lifecycle:lifecycle-runtime-compose:$compose_lifecycle_version"

    // Android
    val androidCoreKtx = "androidx.core:core-ktx:$android_ktx_core_version"

    // Koin
    val koinAndroid = "io.insert-koin:koin-android:$koin_android_version"
    val koinCompose = "io.insert-koin:koin-androidx-compose:$koin_compose_version"

    // Test
    val junit = "junit:junit:$junit_version"
    val mockk = "io.mockk:mockk:$mockk_version"
    val composeJunit = "androidx.compose.ui:ui-test-junit4"
    val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutines_test_version"

    // Misc
    val accompanistPermissions = "com.google.accompanist:accompanist-permissions:$accompanist_perms_version"
}