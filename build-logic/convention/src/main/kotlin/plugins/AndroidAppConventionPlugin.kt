package plugins

import com.android.build.api.dsl.ApplicationExtension
import config.Config
import config.ANDROID_MODULE_ID
import config.NAMESPACE_ID
import extensions.configureAndroidKotlin
import extensions.configureBuildTypes
import extensions.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidAppConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply(pluginId("android-application"))
                apply(pluginId("kotlin-android"))
                apply(pluginId("kotlin-serialization"))
            }
            extensions.configure<ApplicationExtension> {
                namespace = "$NAMESPACE_ID.$ANDROID_MODULE_ID"

                defaultConfig.apply {
                    targetSdk = Config.android.targetSdkVersion
                }

                configureAndroidKotlin(this)
                configureBuildTypes(this)
            }
        }
    }
}
