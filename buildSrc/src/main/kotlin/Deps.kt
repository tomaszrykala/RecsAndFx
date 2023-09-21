object Deps {
    private const val compose_bom_version = "2023.06.01"
    private const val kotlin_bom_version = "1.8.0"
    private const val koin_version = "3.4.2"
    private const val koin_compose_version = "3.4.3"
    private const val compose_navigation_version = "2.7.3"
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

}