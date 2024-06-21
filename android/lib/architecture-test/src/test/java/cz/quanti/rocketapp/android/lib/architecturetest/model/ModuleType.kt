package cz.quanti.rocketapp.android.lib.architecturetest.model

enum class ModuleType(val typeName: String) {
    Library("lib"),
    Feature("feature"),
    App("app"),
    Shared("shared"),
}
