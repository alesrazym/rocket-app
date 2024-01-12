import config.AppBuildType

plugins {
    alias(libs.plugins.quanti.android.application)
    alias(libs.plugins.quanti.android.application.compose)
    alias(libs.plugins.quanti.android.base)
    alias(libs.plugins.quanti.android.detekt)
    alias(libs.plugins.quanti.android.ktlint)
    alias(libs.plugins.kover)
}

android {
    namespace = "cz.quanti.rocketapp"

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
    implementation(projects.android.ui)
    implementation(projects.android.rocket)
    implementation(projects.shared.shared)

    implementation(libs.androidx.appcompat)
    implementation(libs.android.material)
    implementation(libs.koinAndroidCompose)
}

/*
 * Kover configs
 */
dependencies {
    kover(projects.android.ui)
    kover(projects.android.rocket)
    kover(projects.shared.shared)
}

koverReport {
    // filters for all report types of all build variants
    filters {
        excludes {
            classes(
                "*Fragment",
                "*Fragment\$*",
                "*Activity",
                "*Activity\$*",
                "*.databinding.*",
                "*.BuildConfig",
            )
        }
    }

    defaults {
        /**
         * Tests, sources, classes, and compilation tasks of the 'debug' build variant will be included in the default reports.
         * Thus, information from the 'debug' variant will be included in the default report for this project and any project that specifies this project as a dependency.
         *
         * Since the report already contains classes from the JVM target, they will be supplemented with classes from 'debug' build variant of Android target.
         */
        mergeWith("debug")
    }

    androidReports("release") {
        // filters for all report types only of 'release' build type
        filters {
            excludes {
                classes(
                    "*Fragment",
                    "*Fragment\$*",
                    "*Activity",
                    "*Activity\$*",
                    "*.databinding.*",
                    "*.BuildConfig",
                    // excludes debug classes
                    "*.DebugUtil",
                )
            }
        }
    }
}
