package cz.quanti.rocketapp.android.lib.architecturetest.domain

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.ext.list.withParentInterface
import com.lemonappdev.konsist.api.ext.list.withoutParentInterface
import com.lemonappdev.konsist.api.verify.assertEmpty
import cz.quanti.rocketapp.android.lib.architecturetest.model.ArchitectureLayer
import cz.quanti.rocketapp.android.lib.architecturetest.model.Naming
import cz.quanti.rocketapp.android.lib.architecturetest.model.appScope
import cz.quanti.rocketapp.android.lib.architecturetest.model.resideInLayer
import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class DataLayerTest {
    @Test
    fun `Repository implementation check`() {
        Konsist
            .repositoryImplementationClasses()
            .forEach {
                println("Repository implementation check: ${it.name}")

                withClue("${it.name}: Repository implementation should reside in data layer") {
                    it.resideInLayer(ArchitectureLayer.Data) shouldBe true
                }

                withClue("${it.name}: Repository implementation should be internal") {
                    it.hasInternalModifier shouldBe true
                }

                withClue("${it.name}: Repository implementation should be named from repository interface") {
                    it.hasParentInterface { interfaceDeclaration ->
                        interfaceDeclaration.hasNameEndingWith(Naming.REPOSITORY_SUFFIX) &&
                            it.name.endsWith(interfaceDeclaration.name)
                    } shouldBe true
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
