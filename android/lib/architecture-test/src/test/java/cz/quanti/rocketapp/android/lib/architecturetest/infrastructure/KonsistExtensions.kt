package cz.quanti.rocketapp.android.lib.architecturetest.infrastructure

import com.lemonappdev.konsist.api.container.KoScope
import com.lemonappdev.konsist.api.declaration.KoFileDeclaration
import com.lemonappdev.konsist.api.provider.KoHasPackageProvider
import com.lemonappdev.konsist.api.provider.KoResideInPackageProvider
import cz.quanti.rocketapp.android.lib.architecturetest.model.ArchitectureLayer
import cz.quanti.rocketapp.android.lib.architecturetest.model.ModuleType
import cz.quanti.rocketapp.android.lib.architecturetest.model.PackagePatterns
import cz.quanti.rocketapp.android.lib.architecturetest.model.Platform

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

private fun String.toModuleNameInPackage(): String {
    return this
        .replace('\\', '/')
        .split("/")
        .last()
        .replace("-", "")
}
