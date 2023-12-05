plugins {
    id("quanti.android.library")
    id("quanti.android.library.base")
    id("quanti.android.library.compose")
    id("quanti.android.detekt")
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
