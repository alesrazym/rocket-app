import config.AppBuildType

plugins {
    id("quanti.android.application")
    id("quanti.android.application.base")
    id("quanti.android.application.compose")
    id("quanti.android.detekt")
    id("quanti.android.ktlint")
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
}

dependencies {
    // With TYPESAFE_PROJECT_ACCESSORS, we can access projects in dependencies using e.g.
    implementation(projects.ui)
    // instead of
    // implementation(project(":ui"))

    implementation(libs.androidx.appcompat)
    implementation(libs.android.material)
    implementation(libs.koinAndroidCompose)
}
