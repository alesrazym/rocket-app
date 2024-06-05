package plugins

import com.android.build.api.dsl.LibraryExtension
import config.ANDROID_MODULE_ID
import config.NAMESPACE_ID
import extensions.configureAndroidKotlin
import extensions.configureBuildTypes
import extensions.nameNormalized
import extensions.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply(pluginId("android-library"))
                apply(pluginId("kotlin-android"))
                apply(pluginId("kotlin-serialization"))
            }
            extensions.configure<LibraryExtension> {
                namespace = "$NAMESPACE_ID.$ANDROID_MODULE_ID.$nameNormalized"

                configureAndroidKotlin(this)
                configureBuildTypes(this)
            }
        }
    }
}
