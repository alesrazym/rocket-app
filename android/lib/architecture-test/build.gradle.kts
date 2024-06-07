import config.ANDROID_MODULE_ID
import config.MULTIPLATFORM_MODULE_ID
import config.NAMESPACE_ID

plugins {
    alias(libs.plugins.quanti.android.library)
    alias(libs.plugins.quanti.android.base)
    alias(libs.plugins.quanti.android.detekt)
    alias(libs.plugins.kover)
}

android {
    buildFeatures.buildConfig = true

    defaultConfig {
        buildConfigField("String", "ROOT_PACKAGE", "\"$NAMESPACE_ID\"")
        buildConfigField("String", "ANDROID_ID", "\"$ANDROID_MODULE_ID\"")
        buildConfigField("String", "MULTIPLATFORM_ID", "\"$MULTIPLATFORM_MODULE_ID\"")
    }
}

dependencies {
    testImplementation(libs.test.konsist)
}
