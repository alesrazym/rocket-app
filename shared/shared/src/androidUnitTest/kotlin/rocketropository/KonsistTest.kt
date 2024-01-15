package rocketropository

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.ext.list.withoutNameEndingWith
import com.lemonappdev.konsist.api.verify.assertTrue
import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class KonsistTest {
    @Test
    fun `UseCase test`() {
        // TODO: This tests complete project, so, where it should belong?
        //  * shared:shared
        //  * android:app
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

            // TODO: classes with 'UseCase' suffix should have single
            //  'public operator' method named 'invoke'

        }
    }

    // TODO: setup more arch unit tests

}
