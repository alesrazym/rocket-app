// See top level gradle for info on suppression.
import config.AppBuildType

plugins {
    id("quanti.android.application")
    id("quanti.android.application.base")
    id("quanti.android.application.compose")
    id("quanti.android.detekt")
    id("quanti.android.ktlint")

    alias(libs.plugins.androidx.navigationSafeArgsKotlin)
}

android {
    namespace = "cz.quanti.razym.rocketapp"

    defaultConfig {
        applicationId = "cz.quanti.razym.rocketapp"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        debug {
            applicationIdSuffix = AppBuildType.DEBUG.applicationIdSuffix
        }
        release {
            applicationIdSuffix = AppBuildType.RELEASE.applicationIdSuffix
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.swipeRefreshLayout)
    implementation(libs.android.material)
    implementation(libs.bundles.navigation)
}
