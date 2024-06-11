package cz.quanti.rocketapp.android.lib.architecturetest.model

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.container.KoScope

private val modulesInScope = listOf(
    "${PackagePatterns.MULTIPLATFORM_ID}.*",
    "${PackagePatterns.ANDROID_ID}.*",
)

fun Konsist.appScope(): KoScope = scopeFromModules(modulesInScope)

fun Konsist.allScope(): KoScope = scopeFromProject()
    .minus(Konsist.scopeFromModule("build-logic.*"))
