package cz.quanti.rocketapp.android.library.architecturetest.util.model

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.container.KoScope

private val modulesInScope = listOf(
    "${PackagePatterns.MULTIPLATFORM_ID}.*",
    "${PackagePatterns.ANDROID_ID}.*",
)

fun Konsist.appScope(): KoScope {
    return scopeFromModules(modulesInScope)
        // TODO: more clever self reference?
        .minus(Konsist.scopeFromModule("android.library.architecture-test"))
        // TODO: test to scope or not?
//        .minus(Konsist.scopeFromTest())
}
