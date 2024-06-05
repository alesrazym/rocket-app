plugins {
    alias(libs.plugins.quanti.android.library)
    alias(libs.plugins.quanti.android.library.compose)
    alias(libs.plugins.quanti.android.base)
    alias(libs.plugins.quanti.android.detekt)
    alias(libs.plugins.kover)
}

android {
    namespace = "cz.quanti.rocketapp.android.ui"
}
