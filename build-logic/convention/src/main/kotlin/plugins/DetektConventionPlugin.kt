package plugins

import extensions.configureDetekt
import extensions.pluginId
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class DetektConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply(pluginId("detekt"))
            }

            val extension = extensions.getByType<DetektExtension>()
            configureDetekt(extension)
        }
    }
}
