package cz.quanti.razym.rocketapp.data

import cz.quanti.razym.rocketropository.data.RocketData
import cz.quanti.razym.rocketropository.data.RocketsRepositoryImpl
import cz.quanti.razym.rocketropository.data.SpaceXApi
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RocketsRepositoryImplTest {
    private val rocketsData =
        listOf<RocketData>()
/*
        TestUtils.loadJsonResource<List<RocketData>>("rockets.json",
            Types.newParameterizedType(List::class.java, RocketData::class.java))
*/

    @Test
    fun `should convert api result to flow`() = runTest {
        val api = mockk<SpaceXApi> {
            coEvery { listRockets() } returns rocketsData
        }
        val sut = RocketsRepositoryImpl(api)

        val result = sut.getRockets()

        result.first().size shouldBe 4
    }
}