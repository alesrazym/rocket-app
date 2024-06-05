package plugins

import com.android.build.api.dsl.LibraryExtension
import config.Config
import config.MULTIPLATFORM_MODULE_ID
import config.NAMESPACE_ID
import extensions.bundle
import extensions.configureAndroidKotlin
import extensions.configureBuildTypes
import extensions.library
import extensions.libs
import extensions.nameNormalized
import extensions.pluginId
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

class KmpLibConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(pluginManager) {
                apply(pluginId("android-library"))
                apply(pluginId("kotlin-multiplatform"))
                apply(pluginId("kotlin-serialization"))
                apply(pluginId("ksp"))
                apply(pluginId("nativecoroutines"))
            }

            extensions.configure<LibraryExtension> {
                namespace = "$NAMESPACE_ID.$MULTIPLATFORM_MODULE_ID.$nameNormalized"

                configureAndroidKotlin(this)
                configureBuildTypes(this)
            }

            // Because configurations are not ready yet, let's postpone this to afterEvaluate.
            afterEvaluate {
                dependencies {
                    configurations
                        .filter { it.name.startsWith("ksp") && it.name.contains("Test") }
                        .forEach {
                            add(it.name, libs.library("test-mockativeProcessor"))
                        }
                }
            }

            extensions.configure<KotlinMultiplatformExtension> {
                androidTarget {
                    compilations.all {
                        kotlinOptions {
                            jvmTarget = Config.jvm.kotlinJvm
                        }
                    }

                    publishLibraryVariants("release", "debug")
                }

                listOf(
                    iosX64(),
                    iosArm64(),
                    iosSimulatorArm64(),
                ).forEach {
                    it.binaries.framework {
                        baseName = "RocketRepository"
                        isStatic = true
                    }
                }

                // Alternate fix of https://youtrack.jetbrains.com/issue/KT-60734
                // applyDefaultHierarchyTemplate is indeed necessary if you're calling dependsOn manually in your buildscript
                //    applyDefaultHierarchyTemplate()

                with(sourceSets) {
                    // Need to specify, see https://youtrack.jetbrains.com/issue/KT-60734
                    iosMain {}

                    commonSourceSet(libs)
                    androidSourceSet(libs)
                    iosSourceSet(libs)
                    commonTestSourceSet(libs)
                    androidTestSourceSet(libs)

                    all {
                        // Opt-in for com.rickclephas.kmp.nativecoroutines
                        languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
                    }
                }
            }

            // Dummy testClasses, currently needed for multiplatform builds
            tasks.register("testClasses")
        }
    }
}

private fun NamedDomainObjectContainer<KotlinSourceSet>.commonSourceSet(libs: VersionCatalog) {
    getByName("commonMain")
        .apply {
            dependencies {
                implementation(libs.library("koinCore"))
                implementation(libs.bundle("ktor-multiplatform"))
                implementation(libs.library("kotlinx-coroutines"))
            }
        }
}

private fun NamedDomainObjectContainer<KotlinSourceSet>.androidSourceSet(libs: VersionCatalog) {
    getByName("androidMain").apply {
        dependencies {
            implementation(libs.library("ktor-client-android"))
        }
    }
}

private fun NamedDomainObjectContainer<KotlinSourceSet>.iosSourceSet(libs: VersionCatalog) {
    getByName("iosMain").apply {
        dependencies {
            implementation(libs.library("ktor-client-darwin"))
        }
    }
}

private fun NamedDomainObjectContainer<KotlinSourceSet>.commonTestSourceSet(libs: VersionCatalog) {
    getByName("commonTest").apply {
        dependencies {
            implementation(libs.library("test-kotlin"))
            implementation(libs.library("test-resources"))
            implementation(libs.library("test-kotest"))
            implementation(libs.library("test-mockative"))
            implementation(libs.library("test-coroutines"))
        }
    }
}

private fun NamedDomainObjectContainer<KotlinSourceSet>.androidTestSourceSet(libs: VersionCatalog) {
    getByName("androidUnitTest").apply {
        dependencies {
            implementation(libs.library("test-kotlin"))
            implementation(libs.library("test-resources"))
            implementation(libs.library("test-coroutines"))
            implementation(libs.library("test-koin-junit4"))
        }
    }
}
