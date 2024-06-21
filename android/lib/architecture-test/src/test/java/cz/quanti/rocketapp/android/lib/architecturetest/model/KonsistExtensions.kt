package cz.quanti.rocketapp.android.lib.architecturetest.model

import com.lemonappdev.konsist.api.architecture.DependencyRules
import com.lemonappdev.konsist.api.architecture.Layer
import com.lemonappdev.konsist.api.container.KoScope
import com.lemonappdev.konsist.api.declaration.KoFileDeclaration
import com.lemonappdev.konsist.api.provider.KoHasPackageProvider
import com.lemonappdev.konsist.api.provider.KoResideInPackageProvider

fun KoHasPackageProvider.resideInLayer(layer: ArchitectureLayer): Boolean {
    return hasPackage("${PackagePatterns.ROOT_PACKAGE}..${layer.layerName}..")
}

fun KoResideInPackageProvider.resideInLayer(layer: ArchitectureLayer): Boolean {
    return resideInPackage("${PackagePatterns.ROOT_PACKAGE}..${layer.layerName}..")
}

fun KoFileDeclaration.resideInModule(
    platform: Platform,
    moduleType: ModuleType,
    moduleName: String,
): Boolean {
    return if (moduleType in listOf(ModuleType.App, ModuleType.Shared)) {
        hasPackage(
            "${PackagePatterns.ROOT_PACKAGE}." +
                "${platform.platformName}." +
                "${moduleType.typeName}..",
        )
    } else {
        hasPackage(
            "${PackagePatterns.ROOT_PACKAGE}." +
                "${platform.platformName}." +
                "${moduleType.typeName}." +
                "${moduleName.toModuleNameInPackage()}..",
        )
    }
}

fun KoScope.isLayerEmpty(domain: ArchitectureLayer): Boolean {
    return files.none { fileDeclaration ->
        fileDeclaration.resideInLayer(domain)
    }
}

fun KoScope.isLayerEmpty(module: Module): Boolean {
    return files.none { fileDeclaration ->
        fileDeclaration.resideInModule(
            module.platform,
            module.moduleType,
            fileDeclaration.moduleName,
        )
    }
}

fun DependencyRules.checkDependencies(layer: ArchitectureLayer) {
    checkDependencies(
        layer.toKonsistLayer(),
        layer.dependencies()
            .map { it.toKonsistLayer() },
    )
}

fun DependencyRules.checkDependencies(module: Module) {
    checkDependencies(
        module.toKonsistLayer(),
        module.dependencies()
            .map { it.toKonsistLayer() },
    )
}

fun DependencyRules.checkDependencies(
    thisLayer: Layer,
    dependencies: List<Layer>,
) {
    if (dependencies.isEmpty()) {
        thisLayer.dependsOnNothing()
        return
    }

    val arg = dependencies.first()
    val args = dependencies.drop(1)
        .toTypedArray()
    thisLayer.dependsOn(arg, *args)
}

private fun String.toModuleNameInPackage(): String {
    return this
        .replace('\\', '/')
        .split("/")
        .last()
        .replace("-", "")
}
