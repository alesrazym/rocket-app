package plugins

import extensions.pluginId
import org.gradle.api.Plugin
import org.gradle.api.Project

class KtlintConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply(pluginId("kotlinterGradle"))
            }
        }
    }
}
