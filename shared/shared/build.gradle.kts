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

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.shared.rocket)
            }
        }
    }

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

// TODO is it correct?
dependencies {
    // TODO as there is no code in this module, it's probably ok to have
    // dependencies here, not in kotlin block
//    api(projects.shared.rocket)

    kover(projects.shared.rocket)
}
