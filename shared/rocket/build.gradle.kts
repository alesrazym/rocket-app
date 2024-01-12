plugins {
    alias(libs.plugins.quanti.kmp.library)
    alias(libs.plugins.quanti.android.detekt)
    alias(libs.plugins.quanti.android.ktlint)
    alias(libs.plugins.kover)
}

android {
    namespace = "cz.quanti.rocketrepository.rocket"
}
