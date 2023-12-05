package config

import org.gradle.api.JavaVersion

data class AndroidConfig(
    val minSdkVersion: Int,
    val targetSdkVersion: Int,
    val compileSdkVersion: Int,
)

data class JvmConfig(
    val javaVersion: JavaVersion,
    val kotlinJvm: String,
    val freeCompilerArgs: List<String>,
)

object Config {
    val android =
        AndroidConfig(
            minSdkVersion = 26,
            targetSdkVersion = 34,
            compileSdkVersion = 34,
        )
    val jvm =
        JvmConfig(
            javaVersion = JavaVersion.VERSION_17,
            kotlinJvm = JavaVersion.VERSION_17.toString(),
            freeCompilerArgs =
                listOf(
                    "-opt-in=kotlin.RequiresOptIn",
                    // Enable experimental coroutines APIs, including Flow
                    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                    "-opt-in=kotlinx.coroutines.FlowPreview",
                ),
        )
}
