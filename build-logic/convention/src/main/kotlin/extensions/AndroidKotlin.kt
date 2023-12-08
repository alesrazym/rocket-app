package extensions

import com.android.build.api.dsl.CommonExtension
import config.Config
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureAndroidKotlin(extension: CommonExtension<*, *, *, *, *>) {
    with(extension) {
        compileSdk = Config.android.compileSdkVersion

        defaultConfig.apply {
            minSdk = Config.android.minSdkVersion

            testInstrumentationRunner = "androidx.test.runner.AndroidJunitRunner"
            vectorDrawables.useSupportLibrary = true
        }

        compileOptions {
            sourceCompatibility = Config.jvm.javaVersion
            targetCompatibility = Config.jvm.javaVersion
        }

        packaging.resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"

        configureKotlin()

        dependencies {
            add(
                "implementation",
                libs.findLibrary("androidx-coreKtx")
                    .get(),
            )
            add(
                "implementation",
                libs.findLibrary("androidx-fragmentKtx")
                    .get(),
            )
            add(
                "implementation",
                libs.findBundle("lifecycle")
                    .get(),
            )

            add(
                "testImplementation",
                libs.findBundle("test")
                    .get(),
            )
        }
    }
}

private fun Project.configureKotlin() {
    // Use withType to workaround https://youtrack.jetbrains.com/issue/KT-55947
    tasks.withType<KotlinCompile>()
        .configureEach {
            kotlinOptions {
                // Set JVM target to 11
                jvmTarget = Config.jvm.kotlinJvm

                // Treat all Kotlin warnings as errors (disabled by default)
                // Override by setting warningsAsErrors=true in your ~/.gradle/gradle.properties
                val warningsAsErrors: String? by project
                allWarningsAsErrors = warningsAsErrors.toBoolean()
                freeCompilerArgs = freeCompilerArgs + Config.jvm.freeCompilerArgs
            }
        }
}
