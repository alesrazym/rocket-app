import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.quanti.kmp.library)
    alias(libs.plugins.quanti.android.detekt)
    alias(libs.plugins.quanti.android.ktlint)
    alias(libs.plugins.kover)
}

android {
    namespace = "cz.quanti.rocketrepository.shared"
}

val libName = "Shared"
val iosLibFolder = "XCFrameworks/lib"

kotlin {
    val xcframework = XCFramework(libName)

    val iosTargets = listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    )

    iosTargets.forEach {
        it.binaries.framework(libName) {
            xcframework.add(this)
        }
    }
}

val syncIosLibDebug by tasks.creating(Copy::class) {
    dependsOn("assembleSharedDebugXCFramework")
    from(layout.buildDirectory.dir("XCFrameworks/debug"))
    into(layout.buildDirectory.dir(iosLibFolder))
}

val syncIosLibRelease by tasks.creating(Copy::class) {
    dependsOn("assembleSharedReleaseXCFramework")
    from(layout.buildDirectory.dir("XCFrameworks/release"))
    into(layout.buildDirectory.dir(iosLibFolder))
}

dependencies {
    api(projects.shared.rocket)
    kover(projects.shared.rocket)
}
