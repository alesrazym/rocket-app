pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "rocket-app"
include(":app")

check(JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_17)) {
    "Incompatible Java, need to run with Java 17, but found ${JavaVersion.current()}\n\n" +
    "If you are using AndroidStudio, check the settings in File -> Settings -> Build, Execution, Deployment -> Build Tools -> Gradle -> Gradle JDK\n\n" +
    "If you run from command line, check your gradle.properties file in project root and in [user home]/.gradle, and make sure it contains:\n" +
    "org.gradle.java.home=/path/to/jdk-17\n\n" +
    "Alternatively, you can force use of specific JDK using command line param:\n" +
    "gradlew -Dorg.gradle.java.home=/path/to/jdk-17"
}
