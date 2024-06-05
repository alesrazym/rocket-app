plugins {
    alias(libs.plugins.quanti.android.library)
    alias(libs.plugins.quanti.android.base)
    alias(libs.plugins.quanti.android.detekt)
    alias(libs.plugins.kover)
}

android {
    namespace = "cz.quanti.rocketapp.android.library.architecturetest"
}


dependencies {
    testImplementation(libs.test.junit4)
    testImplementation(libs.test.konsist)
}
