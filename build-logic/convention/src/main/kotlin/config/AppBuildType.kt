package config

enum class AppBuildType(val applicationIdSuffix: String? = null) {
    // Here will go staging, beta, or whatever versions.
    DEBUG(".debug"),
    RELEASE,
}
