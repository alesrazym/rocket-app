plugins {
    id("quanti.kmp.library")
    id("quanti.android.detekt")
    id("quanti.android.ktlint")
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
