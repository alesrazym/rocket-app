plugins {
    // TODO multiplatform to convention plugin and catalog
    id("quanti.kmp.library")
    id("com.google.devtools.ksp")
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

    sourceSets {
        // Need to specify, see https://youtrack.jetbrains.com/issue/KT-60734
        iosMain {}

        // TODO choose `getByName().apply` or `val by getting`

        getByName("commonMain").apply {
            dependencies {
                implementation(libs.koinCore)
                implementation(libs.bundles.ktor.multiplatform)
                implementation(libs.kotlinx.coroutines)
                implementation(libs.kotlinx.datetime)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.test.kotlin)
                implementation(libs.test.resources)
                implementation(libs.test.koin.junit4)
                implementation(libs.test.kotest)
                implementation(libs.test.mockative)
                implementation(libs.test.coroutines)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.ktor.client.android)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
    }
}

android {
    namespace = "cz.quanti.razym.rocketrepository"
}

dependencies {
    configurations
        .filter { it.name.startsWith("ksp") && it.name.contains("Test") }
        .forEach {
            add(it.name, libs.test.mockativeProcessor)
        }
}
