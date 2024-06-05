import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.detekt.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApp") {
            id = "quanti.android.application"
            implementationClass = "plugins.AndroidAppConventionPlugin"
        }
        register("androidLib") {
            id = "quanti.android.library"
            implementationClass = "plugins.AndroidLibConventionPlugin"
        }
        register("androidBase") {
            id = "quanti.android.base"
            implementationClass = "plugins.AndroidBaseDependenciesConventionPlugin"
        }
        register("androidAppCompose") {
            id = "quanti.android.application.compose"
            implementationClass = "plugins.AndroidAppComposeConventionPlugin"
        }
        register("androidLibCompose") {
            id = "quanti.android.library.compose"
            implementationClass = "plugins.AndroidLibComposeConventionPlugin"
        }
        register("androidDetekt") {
            id = "quanti.android.detekt"
            implementationClass = "plugins.DetektConventionPlugin"
        }
        register("kmpLib") {
            id = "quanti.kmp.library"
            implementationClass = "plugins.KmpLibConventionPlugin"
        }
    }
}
