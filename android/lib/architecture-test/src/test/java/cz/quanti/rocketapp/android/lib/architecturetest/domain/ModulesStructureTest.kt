package cz.quanti.rocketapp.android.lib.architecturetest.domain

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.architecture.KoArchitectureCreator.assertArchitecture
import com.lemonappdev.konsist.api.verify.assertTrue
import cz.quanti.rocketapp.android.lib.architecturetest.model.Module
import cz.quanti.rocketapp.android.lib.architecturetest.model.ModuleType
import cz.quanti.rocketapp.android.lib.architecturetest.model.Platform
import cz.quanti.rocketapp.android.lib.architecturetest.model.allScope
import cz.quanti.rocketapp.android.lib.architecturetest.model.appScope
import cz.quanti.rocketapp.android.lib.architecturetest.model.checkDependencies
import cz.quanti.rocketapp.android.lib.architecturetest.model.isLayerEmpty
import cz.quanti.rocketapp.android.lib.architecturetest.model.resideInModule
import kotlin.test.Test

class ModulesStructureTest {
    @Test
    fun `modules have correct dependencies`() {
        Konsist
            .appScope()
            .apply {
                assertArchitecture {
                    Module.entries.forEach { module ->
                        if (isLayerEmpty(module)) {
                            return@forEach
                        }

                        checkDependencies(module)
                    }
                }
            }
    }

    @Test
    fun `all files are in some module`() {
        Konsist
            .allScope()
            .files
            .assertTrue { fileDeclaration ->
                ModuleType.entries.any { moduleType ->
                    Platform.entries.any { platform ->
                        fileDeclaration.resideInModule(platform, moduleType, fileDeclaration.moduleName)
                    }
                }
            }
    }
}
