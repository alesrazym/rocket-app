// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.20" apply false
    id("com.google.devtools.ksp") version "1.9.20-1.0.14" apply false

    alias(libs.plugins.detekt) apply false
//    alias(libs.plugins.ktlintGradle) apply false
    alias(libs.plugins.kotlinterGradle) apply false
    alias(libs.plugins.dependencyAnalysis)
}

buildscript {
    // TODO: It would be nice to be able to setup kotlinter within convention plugin.
    //  Configuration with convention plugin is possible for ktlint gradle
    // kotlinter force rules version.
    configurations.classpath {
        resolutionStrategy {
            force(
                libs.ktlint.rule.engine,
                libs.ktlint.rule.engine.core,
                libs.ktlint.cli.reporter.core,
                libs.ktlint.cli.reporter.checkstyle,
                libs.ktlint.cli.reporter.json,
                libs.ktlint.cli.reporter.html,
                libs.ktlint.cli.reporter.plain,
                libs.ktlint.cli.reporter.sarif,
                libs.ktlint.ruleset.standard,
            )
        }
    }

    // kotlinter add rules.
    dependencies {
        classpath(libs.ktlint.rule.compose)
    }
}

dependencyAnalysis {
    // TODO configuration goes here.
    //  Also, configure when to run `buildHealth` task.
    //  Now, this plugin is here as a reminder it exists and may be worth of use.
}
