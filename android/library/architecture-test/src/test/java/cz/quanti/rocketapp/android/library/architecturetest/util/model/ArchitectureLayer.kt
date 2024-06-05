package cz.quanti.rocketapp.android.library.architecturetest.util.model

import com.lemonappdev.konsist.api.architecture.Layer

enum class ArchitectureLayer(val layerName: String) {
    Domain("domain"),
    Presentation("presentation"),
    Data("data"),
    Ui("ui"),
    System("system"),
    Di("di"),
    Util("util")
}

fun ArchitectureLayer.toKonsistLayer(): Layer {
    return Layer(name, "${PackagePatterns.ROOT_PACKAGE}..$layerName..")
}
