// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false

    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.dependencyAnalysis)
}

dependencyAnalysis {
    // TODO: configuration goes here.
    //  Also, configure when to run `buildHealth` task.
    //  Now, this plugin is here as a reminder it exists and may be worth of use.
}
