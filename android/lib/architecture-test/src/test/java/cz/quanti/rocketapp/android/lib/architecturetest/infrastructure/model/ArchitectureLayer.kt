package cz.quanti.rocketapp.android.lib.architecturetest.infrastructure.model

import com.lemonappdev.konsist.api.architecture.Layer

enum class ArchitectureLayer(val layerName: String) {
    /**
     * Can depend on:
     * presentation, data
     *
     * Contains:
     * Screens, Activity, Application, Delegates, Jetpack Compose components, Services, ...
     * Implementation of Dao or Api or other data provider interfaces (if it is required by the tool that is used)
     */
    System("system"),

    /**
     * Can depend on
     * domain, model
     *
     * Contains
     * ViewModels, View State data classes, mappers from domain models
     */
    Presentation("presentation"),

    /**
     * Can depend on
     * model
     *
     * Contains
     * UseCase interfaces + implementation
     * Repository interfaces
     */
    Domain("domain"),

    /**
     * Cannot depend on any other layer
     *
     * Contains
     * domain level models (data classes)
     */
    Model("model"),

    /**
     * Can depend on
     * domain, model
     *
     * Contains
     * Repository implementations
     * data source interfaces (for Dao, Api, ...)
     * data level entities and mappers to model entities
     */
    Data("data"),

    /**
     * Should not depend on other layers
     *
     * Contains
     * general utils/helper/extension functions that extend the functionality of the language or other project wide tool
     */
    Infrastructure("infrastructure"),

    /**
     * Can depend on
     * all layers
     *
     * Contains
     * koin module and definitions
     */
    Di("di"),

    Ui("ui"),
}

fun ArchitectureLayer.toKonsistLayer(): Layer {
    return Layer(name, "${PackagePatterns.ROOT_PACKAGE}..$layerName..")
}

fun ArchitectureLayer.dependencies(): List<ArchitectureLayer> {
    return when (this) {
        ArchitectureLayer.System -> listOf(
            ArchitectureLayer.Presentation,
            ArchitectureLayer.Data,
            ArchitectureLayer.Infrastructure,
        )

        ArchitectureLayer.Presentation -> listOf(
            ArchitectureLayer.Domain,
            ArchitectureLayer.Model,
            ArchitectureLayer.Infrastructure,
        )

        ArchitectureLayer.Domain -> listOf(
            ArchitectureLayer.Model,
            ArchitectureLayer.Infrastructure,
        )

        ArchitectureLayer.Model -> emptyList()

        ArchitectureLayer.Data -> listOf(
            ArchitectureLayer.Domain,
            ArchitectureLayer.Model,
//            ArchitectureLayer.System,
            ArchitectureLayer.Infrastructure,
        )

        ArchitectureLayer.Infrastructure -> emptyList()

        ArchitectureLayer.Di -> listOf(
            ArchitectureLayer.Domain,
            ArchitectureLayer.Data,
            ArchitectureLayer.Presentation,
            ArchitectureLayer.Ui,
            ArchitectureLayer.System,
            ArchitectureLayer.Infrastructure,
        )

        ArchitectureLayer.Ui -> listOf(
            ArchitectureLayer.Presentation,
            ArchitectureLayer.Infrastructure,
        )
    }
}
