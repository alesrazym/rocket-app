package extensions

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project

fun Project.configureBuildTypes(applicationExtension: ApplicationExtension) {
    applicationExtension.apply {
        buildTypes {
            getByName("debug") {
                isMinifyEnabled = false
            }
            getByName("release") {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro",
                )
            }
        }
    }
}

fun Project.configureBuildTypes(libraryExtension: LibraryExtension) {
    libraryExtension.apply {
        defaultConfig {
            consumerProguardFiles("consumer-rules.pro")
        }

        buildTypes {
            getByName("debug") {
                isMinifyEnabled = false
            }
            getByName("release") {
                isMinifyEnabled = false
            }
        }
    }
}
