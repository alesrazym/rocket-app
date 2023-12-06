package cz.quanti.razym.rocketapp.data

import com.squareup.moshi.Types
import cz.quanti.razym.rocketapp.utils.TestUtils
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Test
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

class RocketsRepositoryImplTest {
    private val rocketsData =
        TestUtils.loadJsonResource<List<RocketData>>(
            "rockets.json",
            Types.newParameterizedType(List::class.java, RocketData::class.java),
        )

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
