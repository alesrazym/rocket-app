package cz.quanti.razym.rocketapp.presentation

import cz.quanti.razym.rocketapp.data.RocketData
import cz.quanti.razym.rocketapp.domain.RocketsRepository
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RocketListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

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
        val data = RocketData(name = "Test", first_flight = "1.1.1999")

        val model = data.model()

        model.name shouldBe "Test"
        model.description shouldBe "First flight: 1.1.1999"
    }

    // TODO rfc to separate tests
    @Test
    fun `should handle repository results`() = runTest(testDispatcher) {
        val repository = mockk<RocketsRepository> {
            coEvery { getRockets() } returnsMany listOf(
                flow { emit(emptyList()) },
                // TODO list of rockets from resources, see other test
                flow { emit(listOf(RocketData(name = "Test", first_flight = "1.1.1999"))) },
                flow { throw Exception() }
            )
        }

        val viewModel = RocketListViewModel(repository)

        // Loading state until we let the coroutine in model work with advanceUntilIdle()
        viewModel.uiState.value.state shouldBe UiState.Loading

        advanceUntilIdle()
        viewModel.uiState.value.state shouldBe UiState.Success(emptyList())

        viewModel.fetchRockets()
        advanceUntilIdle()
        val state = viewModel.uiState.value.state as UiState.Success
        state shouldNotBe null
        state.rockets.size shouldBe 1

        viewModel.fetchRockets()
        advanceUntilIdle()
        val error = viewModel.uiState.value.state as UiState.Error
        error shouldNotBe null
    }

    // TODO test separated from ui state??
    @Test
    fun loadRockets() {
    }
}