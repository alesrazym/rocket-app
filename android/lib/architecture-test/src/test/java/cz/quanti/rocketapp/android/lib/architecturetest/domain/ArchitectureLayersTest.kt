package cz.quanti.rocketapp.android.lib.architecturetest.domain

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.architecture.KoArchitectureCreator.assertArchitecture
import com.lemonappdev.konsist.api.verify.assertTrue
import cz.quanti.rocketapp.android.lib.architecturetest.model.ArchitectureLayer
import cz.quanti.rocketapp.android.lib.architecturetest.model.allScope
import cz.quanti.rocketapp.android.lib.architecturetest.model.appScope
import cz.quanti.rocketapp.android.lib.architecturetest.model.checkDependencies
import cz.quanti.rocketapp.android.lib.architecturetest.model.isLayerEmpty
import cz.quanti.rocketapp.android.lib.architecturetest.model.mainAppFileScope
import cz.quanti.rocketapp.android.lib.architecturetest.model.resideInLayer
import kotlin.test.Test

class ArchitectureLayersTest {
    @Test
    fun `clean architecture layers have correct dependencies`() {
        Konsist
            .appScope()
            .minus(mainAppFileScope())
            .apply {
                assertArchitecture {
                    ArchitectureLayer.entries.forEach { layer ->
                        if (isLayerEmpty(layer)) {
                            return@forEach
                        }

                        checkDependencies(layer)
                    }
                }
            }
    }

    @Test
    fun `all files are in some architecture layer`() {
        Konsist
            .allScope()
            .files
            .assertTrue { fileDeclaration ->
                ArchitectureLayer.entries.any { layer ->
                    fileDeclaration.resideInLayer(layer)
                }
            }
    }
}
