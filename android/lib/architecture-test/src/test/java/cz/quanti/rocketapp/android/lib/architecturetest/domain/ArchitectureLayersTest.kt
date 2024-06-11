package cz.quanti.rocketapp.android.lib.architecturetest.domain

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.architecture.KoArchitectureCreator.assertArchitecture
import com.lemonappdev.konsist.api.verify.assertTrue
import cz.quanti.rocketapp.android.lib.architecturetest.infrastructure.isLayerEmpty
import cz.quanti.rocketapp.android.lib.architecturetest.model.allScope
import cz.quanti.rocketapp.android.lib.architecturetest.model.appScope
import cz.quanti.rocketapp.android.lib.architecturetest.infrastructure.resideInLayer
import cz.quanti.rocketapp.android.lib.architecturetest.model.ArchitectureLayer
import cz.quanti.rocketapp.android.lib.architecturetest.model.dependencies
import cz.quanti.rocketapp.android.lib.architecturetest.model.toKonsistLayer
import kotlin.test.Test

class ArchitectureLayersTest {
    @Test
    fun `clean architecture layers have correct dependencies`() {
        // TODO handle MainApplication.kt
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
        Konsist.allScope()
            .files
            .assertTrue { fileDeclaration ->
                ArchitectureLayer.entries.any { layer ->
                    fileDeclaration.resideInLayer(layer)
                }
            }
    }
}
