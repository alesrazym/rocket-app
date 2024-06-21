package cz.quanti.rocketapp.multiplatform.feature.rocket.data

import cz.quanti.rocketapp.multiplatform.feature.rocket.model.rocketsData
import io.kotest.matchers.shouldBe
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.coEvery
import io.mockative.mock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

class RocketsRepositoryImplTest {
    @Mock
    private lateinit var api: SpaceXApi

    @BeforeTest
    fun setup() {
        api = mock(classOf<SpaceXApi>())
    }

    @Test
    fun `should convert api result to flow`() =
        runTest {
            coEvery { api.listRockets() }.returns(rocketsData)

            val sut = DefaultRocketsRepository(api)

            val result = sut.getRockets()
            result.first().size shouldBe 4
        }
}
