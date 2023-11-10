// Top-level build file where you can add configuration options common to all sub-projects/modules.

// As per doc https://developer.android.com/build/migrate-to-catalogs#migrate-plugins
//  If you are using a version of Gradle below 8.1, you need to annotate the plugins{} block with
//  @Suppress("DSL_SCOPE_VIOLATION") when using version catalogs.
//  Refer to issue #22797 https://github.com/gradle/gradle/issues/22797 for more info.

@file:Suppress("DSL_SCOPE_VIOLATION")
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