package cz.quanti.rocketapp.android.lib.architecturetest.model

import com.lemonappdev.konsist.api.architecture.Layer

enum class Module(val moduleType: ModuleType, val platform: Platform) {
    /**
     * depends on all modules
     */
    App(ModuleType.App, Platform.Android),

    /**
     * can depend on any number of android or multiplatform library modules
     *
     * cannot depend on android or multiplatform feature modules
     */
    AndroidLibrary(ModuleType.Library, Platform.Android),

    /**
     * can depend on any number of android or multiplatform library modules
     *
     * can depend on any number of multiplatform feature modules
     *
     * cannot depend on other android feature modules
     */
    AndroidFeature(ModuleType.Feature, Platform.Android),

    /**
     * can depend on any number of other multiplatform library modules
     *
     * cannot depend on feature modules
     */
    MultiplatformLibrary(ModuleType.Library, Platform.Multiplatform),

    /**
     * can depend on any number of multiplatform library modules
     *
     * cannot depend on other multiplatform feature modules
     */
    MultiplatformFeature(ModuleType.Feature, Platform.Multiplatform),

    /**
     * depends on all multiplatform modules
     */
    MultiplatformShared(ModuleType.Shared, Platform.Multiplatform),
}

fun Module.toKonsistLayer(): Layer = Layer(
    name = "${platform.platformName}_${moduleType.typeName}",
    definedBy = "${toPackage()}..",
)

fun Module.toPackage(): String = "${PackagePatterns.ROOT_PACKAGE}.${platform.platformName}.${moduleType.typeName}"

fun Module.dependencies(): List<Module> = when (this) {
    Module.App -> listOf(
        Module.AndroidLibrary,
        Module.AndroidFeature,
        Module.MultiplatformLibrary,
        Module.MultiplatformFeature,
    )

    Module.AndroidFeature -> listOf(
        Module.AndroidLibrary,
        Module.MultiplatformLibrary,
        Module.MultiplatformFeature,
    )

    Module.AndroidLibrary -> listOf(Module.MultiplatformLibrary)

    Module.MultiplatformFeature -> listOf(Module.MultiplatformLibrary)

    Module.MultiplatformLibrary -> emptyList()

    Module.MultiplatformShared -> listOf(
        Module.MultiplatformLibrary,
        Module.MultiplatformFeature,
    )
}
