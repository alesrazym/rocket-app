package plugins

import com.android.build.api.dsl.ApplicationExtension
import extensions.configureAndroidBaseLibs
import extensions.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidAppBaseDependenciesConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply(pluginId("android-application"))
            }
            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroidBaseLibs(extension)
        }
    }
}
