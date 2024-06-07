plugins {
    alias(libs.plugins.quanti.kmp.library)
    alias(libs.plugins.quanti.android.detekt)
    alias(libs.plugins.kover)
}

kotlin {
    // TODO: Looking forward there will be kind of shared test code and resources,
    //  similar to `textFixtures` in Java and Android.
    //  see https://docs.gradle.org/current/userguide/java_testing.html#sec:java_test_fixtures
//    testFixtures {
//        enable = true
//    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.multiplatform.lib.common)
            }
        }
    }
}

// TODO is it correct?
dependencies {
    kover(projects.multiplatform.lib.common)
}
