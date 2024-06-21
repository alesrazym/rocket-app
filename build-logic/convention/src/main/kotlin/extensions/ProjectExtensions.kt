package extensions

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.plugins.DslObject
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

const val MULTIPLATFORM_SHARED = ":multiplatform:shared"

internal inline fun <reified T : Any> Project.extension(block: T.() -> Unit) {
    extensions
        .findByType<T>()
        ?.apply(block)
}

fun Project.envOrProp(key: String) = System.getenv()[key] ?: project.properties[key].toString()

fun Project.kotlin(block: KotlinMultiplatformExtension.() -> Unit) {
    extension(block)
}

fun Project.androidApp(block: ApplicationExtension.() -> Unit) {
    extension(block)
}

fun Project.androidLibrary(block: LibraryExtension.() -> Unit) {
    extension(block)
}

fun Project.androidCommon(block: CommonExtension<*, *, *, *, *>.() -> Unit) {
    // As CommonExtension is a base class, there is no CommonExtension in the project.
    // We need to configure either library or application.
    // As long as `extension` will do nothing when not found,
    // simply try both app and library, they are never both.
    androidApp(block)
    androidLibrary(block)
}

internal fun Project.detekt(block: DetektExtension.() -> Unit) {
    extension(block)
}

/**
 * Get all multiplatform ":lib" and ":feature" projects except:
 *  * :multiplatform:shared
 */
fun Project.allMultiplatformProjects(block: (Project) -> Unit) {
    rootProject.allprojects.forEach { project ->
        project.isMultiplatform {
            val path = project.path
            if (path == MULTIPLATFORM_SHARED) {
                return@isMultiplatform
            }
            if (path.contains(":lib:") || path.contains(":feature:")) {
                block(project)
            }
        }
    }
}

fun Project.isMultiplatform(block: () -> Unit) {
    if (this.path.startsWith(":multiplatform:")) {
        block()
    }
}

val Project.nameNormalized: String
    get() = name.replace("-", "")

@Suppress("UNCHECKED_CAST")
val KotlinMultiplatformExtension.multiplatformSourceSet: NamedDomainObjectContainer<KotlinSourceSet>
    get() {
        val sourceSet = DslObject(this).extensions.getByName("sourceSets")
        return sourceSet as NamedDomainObjectContainer<KotlinSourceSet>
    }

object plugin

/**
 * Syntax sugar.
 * By this syntax I am able to write gradle build this way.
 * class KtPlugin: Plugin<Project> by local plugin { }
 * If the function is not infix or does not extend some object then
 * syntax must be specific this way:
 * class KtPlugin: Plugin<Project> by local plugin({ })
 */
infix fun plugin.implementation(block: Project.() -> Unit) = Plugin<Project> { it.block() }
