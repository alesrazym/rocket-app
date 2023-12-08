// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.androidx.navigationSafeArgsKotlin) apply false
}

// TODO: Don't know if this buildscript is needed, or not.
//  Anyway, this will be removed by compose navigation.
// Project compiles without it, but there is no relevant (lib catalog) documentation on
// https://developer.android.com/guide/navigation/use-graph/safe-args#kts
// In some repositories on github, it's only added to libs.versions.kts, not to buildscript {}.
buildscript {
    repositories {
        google()
    }
    dependencies {
        classpath(libs.androidx.navigationSafeargsPlugin)
    }
}