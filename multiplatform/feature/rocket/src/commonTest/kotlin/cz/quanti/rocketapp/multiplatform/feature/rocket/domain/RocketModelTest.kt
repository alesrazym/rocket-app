package cz.quanti.rocketapp.multiplatform.feature.rocket.domain

import cz.quanti.rocketapp.multiplatform.feature.rocket.infrastructure.rocketsData
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import kotlin.test.Test

class RocketModelTest {
    private val validId = "5e9d0d95eda69955f709d1eb"

    @Test
    fun `should convert data to model`() {
        val data = rocketsData.first { it.id == validId }

        val model = data.asRocketDetail()

        model.id shouldBe validId
        model.name shouldBe "Falcon 1"
        model.overview shouldStartWith "The Falcon 1 was an"
        model.heightMeters shouldBe 22.25
        model.diameterMeters shouldBe 1.68
        model.massTons shouldBe 30.146
        model.firstStage.burnTimeSec shouldBe 169
        model.firstStage.engines shouldBe 1
        model.firstStage.fuelAmountTons shouldBe 44.3
        model.firstStage.reusable shouldBe false
        model.secondStage.burnTimeSec shouldBe 378
        model.secondStage.engines shouldBe 1
        model.secondStage.fuelAmountTons shouldBe 3.38
        model.secondStage.reusable shouldBe false
        model.flickrImages.size shouldBe 2
    }
}
