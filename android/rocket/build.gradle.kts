plugins {
    alias(libs.plugins.quanti.android.library)
    alias(libs.plugins.quanti.android.library.compose)
    alias(libs.plugins.quanti.android.base)
    alias(libs.plugins.quanti.android.detekt)
    alias(libs.plugins.kover)
}

dependencies {
    implementation(projects.android.ui)
    implementation(projects.shared.shared)

    implementation(libs.koinAndroidCompose)
}
