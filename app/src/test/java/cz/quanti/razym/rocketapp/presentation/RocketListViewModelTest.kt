package cz.quanti.razym.rocketapp.presentation

import com.squareup.moshi.Types
import cz.quanti.razym.rocketapp.data.RocketData
import cz.quanti.razym.rocketapp.domain.RocketsRepository
import cz.quanti.razym.rocketapp.presentation.RocketListViewModel.UiState
import cz.quanti.razym.rocketapp.utils.TestUtils
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Date
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class RocketListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val rocketsData =
        TestUtils.loadJsonResource<List<RocketData>>(
            "rockets.json",
            Types.newParameterizedType(List::class.java, RocketData::class.java),
        )

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
            coEvery { getRockets() } returns flow { emit(emptyList()) }
        }

        val viewModel = RocketListViewModel(repository)

        // Loading state until we let the coroutine in model work with advanceUntilIdle()
        viewModel.uiState.value.state shouldBe UiState.Loading

        advanceUntilIdle()
        viewModel.uiState.value.state shouldBe UiState.Success(emptyList())
    }

    @Test
    fun `uiState should be success`() = runTest(testDispatcher) {
        val repository = mockk<RocketsRepository> {
            coEvery { getRockets() } returns flow { emit(rocketsData) }
        }

        val viewModel = RocketListViewModel(repository)

        advanceUntilIdle()
        val success = viewModel.uiState.value.state as UiState.Success
        success shouldNotBe null
        success.rockets.size shouldBe 4
    }

    @Test
    fun `uiState should be error`() = runTest(testDispatcher) {
        val repository = mockk<RocketsRepository> {
            coEvery { getRockets() } returns flow { throw Exception() }
        }

        val viewModel = RocketListViewModel(repository)

        advanceUntilIdle()
        val error = viewModel.uiState.value.state as UiState.Error
        error shouldNotBe null
    }

    @Test
    fun `uiState should be updated after fetch`() = runTest(testDispatcher) {
        val repository = mockk<RocketsRepository> {
            coEvery { getRockets() } returnsMany listOf(
                flow { emit(emptyList()) },
                flow { emit(rocketsData) },
            )
        }

        val viewModel = RocketListViewModel(repository)

        advanceUntilIdle()
        viewModel.uiState.value.state shouldBe UiState.Success(emptyList())

        viewModel.fetchRockets()
        advanceUntilIdle()
        val state = viewModel.uiState.value.state as UiState.Success
        state shouldNotBe null
        state.rockets.size shouldNotBe 0
    }
}
