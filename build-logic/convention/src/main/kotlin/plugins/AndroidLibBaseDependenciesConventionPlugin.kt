package plugins

import com.android.build.gradle.LibraryExtension
import extensions.configureAndroidBaseLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidLibBaseDependenciesConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply("com.android.library")
            }
            val extension = extensions.getByType<LibraryExtension>()
            configureAndroidBaseLibs(extension)
        }
    }
}
