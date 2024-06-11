package cz.quanti.rocketapp.android.lib.architecturetest.model

import cz.quanti.rocketapp.android.architecturetest.BuildConfig

enum class Platform(val platformName: String) {
    Android(BuildConfig.ANDROID_ID),
    Multiplatform(BuildConfig.MULTIPLATFORM_ID),
}
