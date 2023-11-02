package cz.quanti.razym.rocketapp.data

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

import org.junit.Test

class RocketsRepositoryImplTest {
    private val rocketsData: List<RocketData>

    init {
        // TODO resource read & parse to utility class.
        val rocketsJson = javaClass.classLoader?.getResource("rockets.json")?.readText() ?: ""
        val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val type = Types.newParameterizedType(List::class.java, RocketData::class.java)
        val adapter: JsonAdapter<List<RocketData>> = moshi.adapter(type)
        rocketsData = adapter.fromJson(rocketsJson)!!
    }

    @Test
    fun `should convert api result to flow`() = runTest {
        val api = mockk<SpaceXApi>()
        coEvery { api.listRockets() } returns rocketsData
        val sut = RocketsRepositoryImpl(api)

        val result = sut.getRockets()

        result.first().size shouldBe 4
    }
}