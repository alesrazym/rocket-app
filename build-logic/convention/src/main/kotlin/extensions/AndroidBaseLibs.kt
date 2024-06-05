package extensions

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidBaseLibs(
    commonExtension: CommonExtension<*, *, *, *, *>,
) {
    commonExtension.apply {
        dependencies {
            add("implementation", libs.library("koinAndroid"))
            add("implementation", libs.library("coil"))
        }
    }
}
