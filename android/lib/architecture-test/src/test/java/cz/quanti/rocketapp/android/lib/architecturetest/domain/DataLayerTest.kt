package cz.quanti.rocketapp.android.lib.architecturetest.domain

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.print
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.ext.list.withParentInterface
import com.lemonappdev.konsist.api.ext.list.withoutParentInterface
import com.lemonappdev.konsist.api.verify.assertEmpty
import com.lemonappdev.konsist.api.verify.assertTrue
import cz.quanti.rocketapp.android.lib.architecturetest.model.ArchitectureLayer
import cz.quanti.rocketapp.android.lib.architecturetest.model.Naming
import cz.quanti.rocketapp.android.lib.architecturetest.model.appScope
import cz.quanti.rocketapp.android.lib.architecturetest.model.resideInLayer
import io.kotest.assertions.withClue
import kotlin.test.Test

class DataLayerTest {
    @Test
    fun `Repository implementation check`() {
        Konsist
            .repositoryImplementationClasses()
            .print()
            .apply {
                withClue("Repository implementation should reside in data layer") {
                    assertTrue {
                        it.resideInLayer(ArchitectureLayer.Data)
                    }
                }

                withClue("Repository implementation should be internal") {
                    assertTrue {
                        it.hasInternalModifier
                    }
                }

                withClue("Repository implementation should be named from repository interface") {
                    assertTrue {
                        it.hasParentInterface { interfaceDeclaration ->
                            interfaceDeclaration.hasNameEndingWith(Naming.REPOSITORY_SUFFIX) &&
                                it.name.endsWith(interfaceDeclaration.name)
                        }
                    }
                }
            }
    }

    @Test
    fun `Classes ending with Repository suffix must extend one of Repository interfaces`() {
        Konsist
            .otherClasses()
            .withNameEndingWith(Naming.REPOSITORY_SUFFIX)
            .assertEmpty()
    }

    private fun Konsist.repositoryImplementationClasses() = appScope()
        .classes()
        .withParentInterface {
            it.hasNameEndingWith(Naming.REPOSITORY_SUFFIX)
        }

    private fun Konsist.otherClasses() = appScope()
        .classes()
        .withoutParentInterface {
            it.hasNameEndingWith(Naming.REPOSITORY_SUFFIX)
        }
}
