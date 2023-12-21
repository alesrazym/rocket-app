plugins {
    alias(libs.plugins.quanti.kmp.library)
    alias(libs.plugins.quanti.android.detekt)
    alias(libs.plugins.kover)
}

kotlin {
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
}

android {
    namespace = "cz.quanti.razym.rocketrepository"
}
