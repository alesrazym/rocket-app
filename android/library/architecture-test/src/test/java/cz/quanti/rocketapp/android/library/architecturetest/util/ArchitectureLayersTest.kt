@file:Suppress("IllegalIdentifier", "FunctionNaming")

package cz.quanti.rocketapp.android.library.architecturetest.util

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.architecture.KoArchitectureCreator.assertArchitecture
import com.lemonappdev.konsist.api.container.KoScope
import com.lemonappdev.konsist.api.verify.assertTrue
import cz.quanti.rocketapp.android.library.architecturetest.util.model.ArchitectureLayer
import cz.quanti.rocketapp.android.library.architecturetest.util.model.appScope
import cz.quanti.rocketapp.android.library.architecturetest.util.model.toKonsistLayer
import kotlin.test.Test

class ArchitectureLayersTest {
    @Suppress("LongMethod", "CyclomaticComplexMethod")
    @Test
    fun `clean architecture layers have correct dependencies`() {
        Konsist
            .appScope()
            .apply {
                // TODO: There are some empty layers which are filtered out.
                //       Once the layers are filled with classes, the code can be simplified

                // Define layers
                val dependencies = mutableMapOf(
                    ArchitectureLayer.Domain to listOf(ArchitectureLayer.Util).filter { !isLayerEmpty(it) },
                    ArchitectureLayer.Data to listOf(
                        ArchitectureLayer.Domain,
                        ArchitectureLayer.System,
                        ArchitectureLayer.Util,
                    ).filter { !isLayerEmpty(it) },
                    ArchitectureLayer.System to listOf(
                        ArchitectureLayer.Presentation,
                        ArchitectureLayer.Data,
                        ArchitectureLayer.Util,
                    ).filter { !isLayerEmpty(it) },
                    ArchitectureLayer.Presentation to listOf(
                        ArchitectureLayer.Domain,
                        ArchitectureLayer.Util,
                    ).filter { !isLayerEmpty(it) },
                    ArchitectureLayer.Ui to listOf(
                        ArchitectureLayer.Presentation,
                        ArchitectureLayer.Util,
                    ).filter { !isLayerEmpty(it) },
                    ArchitectureLayer.Di to listOf(
                        ArchitectureLayer.Domain,
                        ArchitectureLayer.Data,
                        ArchitectureLayer.Presentation,
                        ArchitectureLayer.Ui,
                        ArchitectureLayer.System,
                        ArchitectureLayer.Util,
                    ).filter { !isLayerEmpty(it) },
                    ArchitectureLayer.Util to emptyList(),
                )

                assertArchitecture {
                    for (layer in ArchitectureLayer.entries) {
                        if (isLayerEmpty(layer)) {
                            continue
                        }

                        dependencies[layer]?.forEach {
                            layer.toKonsistLayer()
                                .dependsOn(it.toKonsistLayer())
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
