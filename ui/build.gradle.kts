plugins {
    alias(libs.plugins.quanti.android.library)
    alias(libs.plugins.quanti.android.base)
    alias(libs.plugins.quanti.android.library.compose)
    alias(libs.plugins.quanti.android.detekt)
    alias(libs.plugins.quanti.android.ktlint)
}

android {
    namespace = "cz.quanti.razym.rocketapp.ui"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
        }
    }
}
