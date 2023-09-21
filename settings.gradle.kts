pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
rootProject.name = "RecsAndFx"

include(
    "app",
    "feature:effect_detail",
    "feature:effects_list",
    "feature:media_player",
    "feature:permissions",
    "core:cpp",
    "core:domain",
    "core:storage",
)