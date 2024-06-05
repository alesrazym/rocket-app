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

fun ArchitectureLayer.dependencies(): List<ArchitectureLayer> {
    return when (this) {
        ArchitectureLayer.Domain -> listOf(ArchitectureLayer.Util)
        ArchitectureLayer.Data -> listOf(
            ArchitectureLayer.Domain,
            ArchitectureLayer.System,
            ArchitectureLayer.Util
        )
        ArchitectureLayer.System -> listOf(
            ArchitectureLayer.Presentation,
            ArchitectureLayer.Data,
            ArchitectureLayer.Util
        )
        ArchitectureLayer.Presentation -> listOf(
            ArchitectureLayer.Domain,
            ArchitectureLayer.Util
        )
        ArchitectureLayer.Ui -> listOf(
            ArchitectureLayer.Presentation,
            ArchitectureLayer.Util
        )
        ArchitectureLayer.Di -> listOf(
            ArchitectureLayer.Domain,
            ArchitectureLayer.Data,
            ArchitectureLayer.Presentation,
            ArchitectureLayer.Ui,
            ArchitectureLayer.System,
            ArchitectureLayer.Util
        )
        ArchitectureLayer.Util -> emptyList()
    }
}
