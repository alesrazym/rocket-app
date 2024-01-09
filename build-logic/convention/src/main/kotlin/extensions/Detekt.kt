package extensions

import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import java.io.File

internal fun Project.configureDetekt(extension: DetektExtension) {
    extension.apply {
        // Version of detekt that will be used. When unspecified the latest detekt
        // version found will be used. Overridden to stay on the same version.
        toolVersion = "1.23.4"
        buildUponDefaultConfig = false
        allRules = false
        config.setFrom("$rootDir/detekt.yml")
        baseline = File("$rootDir/detekt-baseline.xml")

        dependencies {
            "detektPlugins"(libs.findLibrary("detekt-rules").get())
            "detektPlugins"(libs.findLibrary("detekt-formatting").get())
            "detektPlugins"(libs.findLibrary("detekt-compose").get())
        }
    }
}
