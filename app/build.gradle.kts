plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    // TODO: Create a ConventionPlugin for detekt and ktlint configuration
    alias(libs.plugins.detekt)
//    alias(libs.plugins.ktlintGradle)
    alias(libs.plugins.kotlinterGradle)
}

/*
ktlint {
    debug.set(true)
    android.set(true)
    version.set("1.0.1")
}
*/

detekt {
    // Version of detekt that will be used. When unspecified the latest detekt
    // version found will be used. Overridden to stay on the same version.
    toolVersion = "1.23.4"
    buildUponDefaultConfig = false
    allRules = false
    config.setFrom("$rootDir/detekt.yml")
    baseline = File("$rootDir/detekt-baseline.xml")
}

android {
    namespace = "cz.quanti.razym.rocketapp"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "cz.quanti.razym.rocketapp"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlinCompilerExtensionVersion.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // With TYPESAFE_PROJECT_ACCESSORS, we can access projects in dependencies using e.g.
    implementation(projects.ui)
    // instead of
    // implementation(project(":ui"))

    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.appcompat)
    implementation(libs.android.material)
    implementation(platform(libs.androidx.composeBom))
    implementation(libs.bundles.compose)

    implementation(libs.bundles.koin)
    implementation(libs.moshiKotlin)
    implementation(libs.bundles.retrofit)
    implementation(libs.coil)

    testImplementation(libs.bundles.test)

    detektPlugins(libs.detekt.rules)
//    detektPlugins(libs.detekt.formatting)
//    detektPlugins(libs.detekt.compose)

//    ktlintRuleset(libs.ktlint.rule.compose)
}
