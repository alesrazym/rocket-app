package cz.quanti.razym.rocketapp.presentation

import com.squareup.moshi.Types
import cz.quanti.razym.rocketapp.data.RocketData
import cz.quanti.razym.rocketapp.domain.RocketsRepository
import cz.quanti.razym.rocketapp.utils.TestUtils
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class RocketListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val rocketsData =
        TestUtils.loadJsonResource<List<RocketData>>("rockets.json",
            Types.newParameterizedType(List::class.java, RocketData::class.java))

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should convert data to model`() {
        val data = rocketsData.first { it.id == "5e9d0d95eda69955f709d1eb" }

        val model = data.asRocket()

        model.id shouldBe "5e9d0d95eda69955f709d1eb"
        model.name shouldBe "Falcon 1"
        model.firstFlight shouldBe Date(1143154800L * 1000)
    }

    @Test
    fun `uiState should be loading first`() = runTest(testDispatcher) {
        val repository = mockk<RocketsRepository> {
            coEvery { getRockets() } returns flow {
                delay(1000)
                emit(emptyList())
            }
        }

        val viewModel = rocketListViewModel(repository)

        // Loading state until we let the coroutine in model work with advanceUntilIdle()
        advanceTimeBy(1)
        viewModel.uiState.value.let {
            it.loading shouldBe true
            it.rockets shouldBe null
        }

        advanceUntilIdle()
        viewModel.uiState.value.let {
            it.loading shouldBe false
            it.rockets shouldBe emptyList()
        }
    }

    @Test
    fun `uiState should be success`() = runTest(testDispatcher) {
        val repository = mockk<RocketsRepository> {
            coEvery { getRockets() } returns flow { emit(rocketsData) }
        }

        val viewModel = rocketListViewModel(repository)

        advanceUntilIdle()
        viewModel.uiState.value.let {
            it.loading shouldBe false
            it.rockets shouldNotBe null
            it.rockets?.size shouldBe 4
        }
    }

    @Test
    fun `uiState should be error`() = runTest(testDispatcher) {
        val repository = mockk<RocketsRepository> {
            coEvery { getRockets() } returns flow { throw Exception() }
        }

        val viewModel = rocketListViewModel(repository)

        advanceUntilIdle()
        viewModel.uiState.value.let {
            it.loading shouldBe false
            it.rockets shouldBe null
            it.messages.size shouldBe 1
        }
    }

    @Test
    fun `uiState should be updated after fetch`() = runTest(testDispatcher) {
        val repository = mockk<RocketsRepository> {
            coEvery { getRockets() } returnsMany listOf(
                flow { emit(emptyList()) },
                flow { emit(rocketsData) }
            )
        }

        val viewModel = rocketListViewModel(repository)

        advanceUntilIdle()
        viewModel.uiState.value.let {
            it.loading shouldBe false
            it.rockets shouldBe emptyList()
        }

        viewModel.fetchRockets()
        advanceUntilIdle()
        viewModel.uiState.value.let {
            it.loading shouldBe false
            it.rockets shouldNotBe null
            it.rockets?.size shouldBe 4
        }
    }

    private fun rocketListViewModel(repository: RocketsRepository): RocketListViewModel {
        val viewModel = RocketListViewModel(repository)
        viewModel.initialize()
        return viewModel
    }
}
