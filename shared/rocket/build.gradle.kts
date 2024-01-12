plugins {
    alias(libs.plugins.quanti.kmp.library)
    alias(libs.plugins.quanti.android.detekt)
    alias(libs.plugins.kover)
}

android {
    namespace = "cz.quanti.rocketrepository.rocket"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.shared.common)
            }
        }
    }
}

// TODO is it correct?
dependencies {
    kover(projects.shared.common)
}
