plugins {
    alias(libs.plugins.quanti.android.library)
    alias(libs.plugins.quanti.android.library.compose)
    alias(libs.plugins.quanti.android.base)
    alias(libs.plugins.quanti.android.detekt)
    alias(libs.plugins.quanti.android.ktlint)
    alias(libs.plugins.kover)
}

android {
    namespace = "cz.quanti.rocketapp.android.rocket"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
        }
    }
}

dependencies {
    implementation(projects.android.ui)
    implementation(projects.shared.rocket)
}
