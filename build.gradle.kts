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
    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }
}
