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
    // With TYPESAFE_PROJECT_ACCESSORS
    // instead of `implementation(project(":ui"))`
    // we can access projects in dependencies as follows:
    implementation(projects.ui)
    implementation(projects.rocketRepository)

    implementation(libs.androidx.appcompat)
    implementation(libs.android.material)
    implementation(libs.koinAndroidCompose)
    implementation(libs.kotlinx.datetime)
}
