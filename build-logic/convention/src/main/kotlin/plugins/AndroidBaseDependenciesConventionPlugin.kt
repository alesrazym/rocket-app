package plugins

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import extensions.configureAndroidBaseLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType

class AndroidBaseDependenciesConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            extensions.findByType<ApplicationExtension>()?.let { extension ->
                configureAndroidBaseLibs(extension)
            }
            extensions.findByType<LibraryExtension>()?.let { extension ->
                configureAndroidBaseLibs(extension)
            }
        }
    }
}
