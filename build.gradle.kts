plugins {
    /**
     * Use `apply false` in the top-level build.gradle file to add a Gradle
     * plugin as a build dependency but not apply it to the current (root)
     * project. Don't use `apply false` in sub-projects. For more information,
     * see Applying external plugins with same version to subprojects.
     */

    id("com.android.application") version "8.0.2" apply false // "8.1.1"
    id("com.android.library") version "8.0.2" apply false // "8.1.1"
    id("org.jetbrains.kotlin.android") version "1.7.20" apply false // "1.8.22"
}

buildscript {
//    ext {
//        kotlin_bom_version = "1.8.0"
//        koin_version = "3.4.2"
//        koin_compose_version = "3.4.3"
//        compose_bom_version = "2023.06.01"
//        compose_navigation_version = "2.7.3"
//        accompanist_perms_version = "0.30.1"
//        junit_version = "4.13.2"
//        mockk_version = "1.13.5"
//        coroutines_test_version = "1.7.3"
//    }
    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }
}
