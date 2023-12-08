// See top level gradle for info on suppression.
import config.AppBuildType

plugins {
    id("quanti.android.application")
    id("quanti.android.application.base")

    alias(libs.plugins.androidx.navigationSafeArgsKotlin)

    // TODO: Create a ConventionPlugin for detekt and ktlint configuration
    alias(libs.plugins.detekt)
//    alias(libs.plugins.ktlintGradle)
    alias(libs.plugins.kotlinterGradle)
}

/*
ktlint {
    debug.set(true)
    android.set(true)
    version.set("1.0.1")
}
*/

detekt {
    // Version of detekt that will be used. When unspecified the latest detekt
    // version found will be used. Overridden to stay on the same version.
    toolVersion = "1.23.4"
    buildUponDefaultConfig = false
    allRules = false
    config.setFrom("$rootDir/detekt.yml")
    baseline = File("$rootDir/detekt-baseline.xml")
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
        compose = true
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlinCompilerExtensionVersion.get()
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.swipeRefreshLayout)
    implementation(libs.android.material)
    implementation(platform(libs.androidx.composeBom))
    implementation(libs.bundles.compose)
    implementation(libs.bundles.navigation)

    detektPlugins(libs.detekt.rules)
//    detektPlugins(libs.detekt.formatting)
//    detektPlugins(libs.detekt.twitter.compose)

//    ktlintRuleset(libs.ktlint.twitter.compose)
}
