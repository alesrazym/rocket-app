package cz.quanti.razym.rocketapp.presentation

import cz.quanti.razym.rocketapp.data.RocketData
import io.kotest.matchers.shouldBe
import org.junit.Test

class RocketListViewModelTest {

    @Test
    fun `should convert data to model`() {
        val data = RocketData(name = "Test", first_flight = "1.1.1999")

        val model = data.model()

        model.name shouldBe "Test"
        model.description shouldBe "First flight: 1.1.1999"
    }
}