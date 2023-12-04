plugins {
    // TODO multiplatform to convention plugin and catalog
    id("quanti.kmp.library")
    kotlin("plugin.serialization")
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

    val ktor = "3.0.0-beta-1"
    val koin = "3.5.0"
    sourceSets {
        // Need to specify, see https://youtrack.jetbrains.com/issue/KT-60734
        iosMain {}

        // TODO choose `getByName().apply` or `val by getting`

        getByName("commonMain").apply {
            dependencies {
                implementation("io.insert-koin:koin-core:$koin")
                implementation("io.ktor:ktor-client-core:$ktor")
                implementation("io.ktor:ktor-client-content-negotiation:$ktor")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor")
                implementation(libs.kotlinx.coroutines)
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("com.goncalossilva:resources:0.4.0")
                implementation(libs.test.koin.junit4)
                implementation(libs.test.kotest)
                implementation("io.mockative:mockative:2.0.1")
                implementation(libs.test.coroutines)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-android:$ktor")
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

dependencies {
    configurations
        .filter { it.name.startsWith("ksp") && it.name.contains("Test") }
        .forEach {
            add(it.name, "io.mockative:mockative-processor:1.3.1")
        }
}
