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
        /*
         Fix to the error:
         > A failure occurred while executing com.android.build.gradle.internal.tasks.MergeJavaResWorkAction
         > 2 files found with path 'META-INF/versions/9/previous-compilation-data.bin' from inputs:
           - [.gradle]\caches\modules-2\files-2.1\org.jetbrains.kotlinx\kotlinx-datetime-jvm\0.4.1\684eec210b21e2da7382a4aa85e56fb7b71f39b3\kotlinx-datetime-jvm-0.4.1.jar
           - [.gradle]\caches\modules-2\files-2.1\org.jetbrains.kotlinx\atomicfu-jvm\0.22.0\c6a128a44ba52a18265e5ec816130cd341d80792\atomicfu-jvm-0.22.0.jar
         */
        packaging.resources.excludes += "/META-INF/versions/**"
        // End of fix

        configureKotlin()

        dependencies {
            add("implementation", libs.library("androidx-coreKtx"))
            add("implementation", libs.bundle("test"))
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
