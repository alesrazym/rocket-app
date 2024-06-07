@file:Suppress("IllegalIdentifier", "FunctionNaming")

package cz.quanti.rocketapp.android.lib.architecturetest.util

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.architecture.KoArchitectureCreator.assertArchitecture
import com.lemonappdev.konsist.api.container.KoScope
import com.lemonappdev.konsist.api.verify.assertTrue
import cz.quanti.rocketapp.android.lib.architecturetest.util.model.ArchitectureLayer
import cz.quanti.rocketapp.android.lib.architecturetest.util.model.appScope
import cz.quanti.rocketapp.android.lib.architecturetest.util.model.dependencies
import cz.quanti.rocketapp.android.lib.architecturetest.util.model.toKonsistLayer
import kotlin.test.Test

class ArchitectureLayersTest {
    @Suppress("LongMethod", "CyclomaticComplexMethod")
    @Test
    fun `clean architecture layers have correct dependencies`() {
        Konsist.appScope()
            .apply {
                assertArchitecture {
                    for (layer in ArchitectureLayer.entries) {
                        if (isLayerEmpty(layer)) {
                            continue
                        }

                        val konsistLayer = layer.toKonsistLayer()
                        layer.dependencies()
                            .filter { !isLayerEmpty(it) }
                            .forEach {
                                konsistLayer.dependsOn(it.toKonsistLayer())
                            }
                    }
                }
            }
    }

    @Test
    fun `all files are in some architecture layer`() {
        Konsist.appScope()
            .files
            .assertTrue { fileDeclaration ->
                ArchitectureLayer.entries.any { layer ->
                    fileDeclaration.resideInLayer(layer)
                }
            }
    }
}

private fun KoScope.isLayerEmpty(domain: ArchitectureLayer): Boolean {
    return files.none { fileDeclaration ->
        fileDeclaration.resideInLayer(domain)
    }
}
