package cz.quanti.rocketapp.android.lib.architecturetest

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.declaration.KoClassDeclaration
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.ext.list.withoutNameEndingWith
import com.lemonappdev.konsist.api.verify.assertTrue
import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class KonsistTest {
    @Test
    fun `UseCase test`() {
        Konsist.scopeFromProject().apply {
            withClue("UseCase should reside in domain package") {
                classes()
                    .withNameEndingWith("UseCaseImpl")
                    .forEach {
                        println("Checking $it reside in domain package.")
                        it.resideInPackage("..domain..") shouldBe true
                        it.hasInternalModifier shouldBe true
                    }
                interfaces()
                    .withNameEndingWith("UseCase")
                    .assertTrue {
                        println("Checking $it reside in domain package.")
                        it.resideInPackage("..domain..")
                    }
            }

            withClue("UseCase should be only suffixed with UseCase / UseCaseImpl") {
                classes()
                    .withoutNameEndingWith("UseCaseImpl")
                    .forEach {
                        it.hasParentWithName("UseCase") shouldBe false
                    }
                interfaces()
                    .withoutNameEndingWith("UseCase")
                    .forEach {
                        it.hasParentWithName("UseCase") shouldBe false
                    }
            }

            withClue("UseCase should have single 'invoke' method") {
                classes()
                    .withNameEndingWith("UseCase")
                    .forEach(::checkInvokeFunction)
            }
        }
    }

    private fun checkInvokeFunction(classDeclaration: KoClassDeclaration) {
        with(classDeclaration) {
            numFunctions() shouldBe 1
            countFunctions { it.name == "invoke" } shouldBe 1
            functions().single().run {
                hasPublicModifier shouldBe true
                hasOperatorModifier shouldBe true
            }
        }
    }
}
