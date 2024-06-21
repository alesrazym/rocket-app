package cz.quanti.rocketapp.android.lib.architecturetest.model

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.container.KoScope

private val modulesInScope = listOf(
    "${PackagePatterns.MULTIPLATFORM_ID}.*",
    "${PackagePatterns.ANDROID_ID}.*",
)

fun Konsist.appScope(): KoScope = scopeFromModules(modulesInScope)

fun mainAppFileScope(): KoScope {
    val module = Module.App
    val packageAsPath = module.toPackage().replace(".", "/")

    return Konsist.scopeFromFile(
        "${module.platform.platformName}/${module.moduleType.typeName}/" +
            "src/main/java/" +
            "$packageAsPath/" +
            "${ArchitectureLayer.System.layerName}/MainApplication.kt",
    )
}

fun Konsist.allScope(): KoScope = scopeFromProject()
    .minus(Konsist.scopeFromModule("build-logic.*"))
