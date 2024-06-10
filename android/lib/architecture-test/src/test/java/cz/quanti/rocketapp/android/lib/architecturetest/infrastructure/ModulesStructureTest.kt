@file:Suppress("IllegalIdentifier", "FunctionNaming")

package cz.quanti.rocketapp.android.lib.architecturetest.infrastructure

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.architecture.KoArchitectureCreator.assertArchitecture
import com.lemonappdev.konsist.api.verify.assertTrue
import cz.quanti.rocketapp.android.lib.architecturetest.infrastructure.model.ModuleType
import cz.quanti.rocketapp.android.lib.architecturetest.infrastructure.model.Platform
import cz.quanti.rocketapp.android.lib.architecturetest.infrastructure.model.allScope
import cz.quanti.rocketapp.android.lib.architecturetest.infrastructure.model.appScope
import cz.quanti.rocketapp.android.lib.architecturetest.infrastructure.model.toKonsistLayer
import kotlin.test.Ignore
import kotlin.test.Test

class ModulesStructureTest {
    @Test
    fun `modules have correct dependencies`() {
        Konsist
            .appScope()
            .assertArchitecture {
                // Define layers
                val libraryMultiplatform = ModuleType.Library.toKonsistLayer(Platform.Multiplatform)
                val featureMultiplatform = ModuleType.Feature.toKonsistLayer(Platform.Multiplatform)
//                val sharedMultiplatform = ModuleType.Shared.toKonsistLayer(Platform.Multiplatform)
                val libraryAndroid = ModuleType.Library.toKonsistLayer(Platform.Android)
                val featureAndroid = ModuleType.Feature.toKonsistLayer(Platform.Android)
                val appAndroid = ModuleType.App.toKonsistLayer(Platform.Android)

                // Define architecture assertions
                libraryMultiplatform.dependsOnNothing()
                libraryAndroid.dependsOn(libraryMultiplatform)
                featureMultiplatform.dependsOn(libraryMultiplatform)
//                sharedMultiplatform.dependsOn(featureMultiplatform, libraryMultiplatform)
                featureAndroid.dependsOn(libraryAndroid, featureMultiplatform)
                appAndroid.dependsOn(
                    featureAndroid,
                    libraryAndroid,
//                    sharedMultiplatform,
                    featureMultiplatform,
                    libraryMultiplatform,
                )
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

    @Test
    @Ignore
    fun `only architecture, common and logger core modules can be accessed from feature modules`() {
        // TODO will be implemented in future (Azure task number: 89760)
    }
}
