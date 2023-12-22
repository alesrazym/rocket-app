pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    // PREFER_PROJECT as I don't know the iOS ivy repository..
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "rocket-app"

// With TYPESAFE_PROJECT_ACCESSORS, we can access projects in dependencies using e.g.
//  `implementation(projects.ui)`
// See https://docs.gradle.org/current/userguide/declaring_dependencies.html#sec:type-safe-project-accessors.
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include("android:app")
include(":android:rocket")
include("android:ui")
include("shared:rocket")

check(JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_17)) {
    "Incompatible Java, need to run with Java 17, but found ${JavaVersion.current()}\n\n" +
        "If you are using AndroidStudio, check the settings in " +
        "File -> Settings -> Build, Execution, Deployment -> Build Tools -> Gradle -> Gradle JDK\n\n" +
        "If you run from command line, check your gradle.properties file in project root and in [user home]/.gradle, " +
        "and make sure it contains:\n" +
        "org.gradle.java.home=/path/to/jdk-17\n\n" +
        "Alternatively, you can force use of specific JDK using command line param:\n" +
        "gradlew -Dorg.gradle.java.home=/path/to/jdk-17"
}
