package cz.quanti.rocketapp.android.lib.architecturetest.domain

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.declaration.KoClassDeclaration
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.ext.list.withParentInterfaceOf
import com.lemonappdev.konsist.api.ext.list.withoutName
import com.lemonappdev.konsist.api.ext.list.withoutParentInterfaceOf
import com.lemonappdev.konsist.api.verify.assertEmpty
import com.lemonappdev.konsist.api.verify.assertTrue
import cz.quanti.rocketapp.android.lib.architecturetest.infrastructure.resideInLayer
import cz.quanti.rocketapp.android.lib.architecturetest.model.ArchitectureLayer
import cz.quanti.rocketapp.android.lib.architecturetest.model.Naming
import cz.quanti.rocketapp.android.lib.architecturetest.model.appScope
import cz.quanti.rocketapp.multiplatform.lib.common.domain.UseCase
import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import kotlin.reflect.KClass
import kotlin.test.Test

class DomainLayerTest {
    private val useCaseBaseInterface = UseCase::class

    private val useCaseInterfaces =
        mutableListOf<KClass<out UseCase<*, *>>>(useCaseBaseInterface)
            .apply {
                addAll(useCaseBaseInterface.sealedSubclasses)
            }.toList()

    @Test
    fun `UseCase interfaces check`() {
        Konsist
            .useCaseInterfaces()
            .apply {
                withClue("UseCase should reside in domain layer") {
                    assertTrue {
                        it.resideInLayer(ArchitectureLayer.Domain)
                    }
                }

                withClue("UseCase interface should be only suffixed with UseCase") {
                    assertTrue {
                        it.hasNameEndingWith(Naming.USECASE_SUFFIX)
                    }
                }
            }
    }

    @Test
    fun `Interfaces ending with UseCase suffix must extend one of UseCase`() {
        Konsist
            .otherInterfaces()
            .withNameEndingWith(Naming.USECASE_SUFFIXES)
            .assertEmpty()
    }

    @Test
    fun `UseCase classes check`() {
        Konsist
            .useCaseClasses()
            .apply {
                withClue("UseCase should reside in domain layer") {
                    assertTrue {
                        it.resideInLayer(ArchitectureLayer.Domain)
                    }
                }

                withClue("UseCase class should be only suffixed with UseCaseImpl") {
                    assertTrue {
                        it.hasNameEndingWith(Naming.USECASE_IMPL_SUFFIX)
                    }
                }

                withClue("UseCase should have single 'invoke' method") {
                    forEach(::checkInvokeFunction)
                }
            }
    }

    @Test
    fun `Classes ending with UseCase suffix must extend one of UseCase`() {
        Konsist
            .otherClasses()
            .withNameEndingWith(Naming.USECASE_SUFFIXES)
            .assertEmpty()
    }

    private fun Konsist.useCaseInterfaces() =
        appScope()
            .interfaces()
            .withParentInterfaceOf(useCaseBaseInterface, indirectParents = true)

    private fun Konsist.otherInterfaces() =
        appScope()
            .interfaces()
            .withoutParentInterfaceOf(useCaseBaseInterface, indirectParents = true)
            .withoutName(useCaseInterfaces.mapNotNull { it.simpleName })

    private fun Konsist.useCaseClasses() =
        appScope()
            .classes()
            .withParentInterfaceOf(useCaseBaseInterface, indirectParents = true)

    private fun Konsist.otherClasses() =
        appScope()
            .classes()
            .withoutParentInterfaceOf(useCaseBaseInterface, indirectParents = true)

    private fun checkInvokeFunction(classDeclaration: KoClassDeclaration) {
        with(classDeclaration) {
            numFunctions() shouldBe 1
            countFunctions { it.name == "invoke" } shouldBe 1
            functions().single()
                .run {
                    hasPublicOrDefaultModifier shouldBe true
                }
        }
    }

    // TODO add tests for Repositories and domain models (Azure task number: 89759)

}
