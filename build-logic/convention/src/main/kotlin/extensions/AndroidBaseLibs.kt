package extensions

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidBaseLibs(
    commonExtension: CommonExtension<*, *, *, *, *>,
) {
    commonExtension.apply {
        dependencies {
            add("implementation", libs.findLibrary("koinAndroid").get())
            add("implementation", libs.findLibrary("moshiKotlin").get())
            add("implementation", libs.findLibrary("coil").get())
            add("implementation", libs.findBundle("retrofit").get())
        }
    }
}
