package extensions

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures.compose = true

        composeOptions.kotlinCompilerExtensionVersion = libs.version("kotlinCompilerExtensionVersion")

        dependencies {
            add("implementation", platform(libs.library("androidx-composeBom")))
            add("implementation", libs.bundle("compose"))
        }
    }
}
