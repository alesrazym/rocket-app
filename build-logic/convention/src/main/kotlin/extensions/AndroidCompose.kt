package extensions

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures.compose = true

        composeOptions.kotlinCompilerExtensionVersion =
            libs.findVersion("kotlinCompilerExtensionVersion").get().toString()

        dependencies {
            // TODO update rule so that this remains on one line.
            add("implementation", platform(libs.findLibrary("androidx-composeBom").get()))
            add("implementation", libs.findBundle("compose").get())
        }
    }
}
