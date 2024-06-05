package cz.quanti.rocketropository.data

import com.goncalossilva.resources.Resource
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json

class RocketsRepositoryImplAndroidUnitTest {
    private val json =
        Json {
            ignoreUnknownKeys = true
        }

    private val rocketsData: List<RocketData> =
        json.decodeFromString(
            Resource(path = "src/commonTest/resources/rockets.json").readText(),
        )

    private lateinit var api: SpaceXApi

    @BeforeTest
    fun setup() {
        api = mockk()
    }

    @Test
    fun `should convert api result to flow`() =
        runTest {
            coEvery { api.listRockets() }.returns(rocketsData)

            val sut = RocketsRepositoryImpl(api)

            val result = sut.getRockets()
            result.first().size shouldBe 4
        }
}
