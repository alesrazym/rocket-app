plugins {
    // TODO multiplatform to convention plugin and catalog
    id("quanti.kmp.library")
    id("quanti.android.detekt")
    id("quanti.android.ktlint")
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
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

    val ktor = "3.0.0-beta-1"
    val coroutines = "1.7.3"
    sourceSets {
        // Need to specify, see https://youtrack.jetbrains.com/issue/KT-60734
        iosMain {}

        getByName("commonMain").apply {
            dependencies {
                implementation("io.ktor:ktor-client-core:$ktor")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:$ktor")
            }
        }
        val iosMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-darwin:$ktor")
            }
        }
    }
}

android {
    namespace = "cz.quanti.razym.rocketrepository"
}
