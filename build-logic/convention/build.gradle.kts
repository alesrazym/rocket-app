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
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
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
        register("androidAppBase") {
            id = "quanti.android.application.base"
            implementationClass = "plugins.AndroidAppBaseDependenciesConventionPlugin"
        }
        register("androidLibBase") {
            id = "quanti.android.library.base"
            implementationClass = "plugins.AndroidLibBaseDependenciesConventionPlugin"
        }
        register("androidAppCompose") {
            id = "quanti.android.application.compose"
            implementationClass = "plugins.AndroidAppComposeConventionPlugin"
        }
        register("androidLibCompose") {
            id = "quanti.android.lib.compose"
            implementationClass = "plugins.AndroidLibComposeConventionPlugin"
        }
    }
}
