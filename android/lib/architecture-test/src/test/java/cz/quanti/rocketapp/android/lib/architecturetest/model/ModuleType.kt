package cz.quanti.rocketapp.android.lib.architecturetest.model

import com.lemonappdev.konsist.api.architecture.Layer

enum class ModuleType(val typeName: String) {
    Library("lib"),
    Feature("feature"),

    // TODO: use or not?
//    Core("core"),
    App("app"),
    Shared("shared"),
}

fun ModuleType.toKonsistLayer(platform: Platform): Layer = Layer(
    name = "${platform.platformName}_$typeName",
    definedBy = "${PackagePatterns.ROOT_PACKAGE}.${platform.platformName}.$typeName..",
)
