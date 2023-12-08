plugins {
    id("quanti.android.library")
    id("quanti.android.library.base")
}

android {
    namespace = "cz.quanti.razym.rocketapp.ui"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlinCompilerExtensionVersion.get()
    }
}

dependencies {
    implementation(platform(libs.androidx.composeBom))
    implementation(libs.bundles.compose)
}
