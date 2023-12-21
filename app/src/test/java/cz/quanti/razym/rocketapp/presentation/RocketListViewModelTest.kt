package cz.quanti.razym.rocketapp.presentation

import android.net.http.HttpException
import android.util.MalformedJsonException
import cz.quanti.razym.rocketapp.R
import cz.quanti.razym.rocketapp.utils.rocketsData
import cz.quanti.razym.rocketropository.data.RocketData
import cz.quanti.razym.rocketropository.domain.RocketsRepository
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.util.Date
import java.util.concurrent.TimeoutException
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

@OptIn(ExperimentalCoroutinesApi::class)
class RocketListViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val rocketsData: List<RocketData> = rocketsData()

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
        model.firstFlight shouldBe Date(1143158400L * 1000)
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
        viewModel.uiState.value is UiScreenState.Loading

        advanceUntilIdle()
        viewModel.uiState.value shouldBe UiScreenState.Data(emptyList())
    }

    @Test
    fun `uiState should be success`() = runTest(testDispatcher) {
        val repository = mockk<RocketsRepository> {
            coEvery { getRockets() } returns flow { emit(rocketsData) }
        }

        val viewModel = rocketListViewModel(repository)

        advanceUntilIdle()
        viewModel.uiState.value shouldBe UiScreenState.Data(rocketsData.map { it.asRocket() })
    }

    // TODO can we use parametrized test case? How will we test in KMP?
    @Test
    fun `uiState should be error on Exception`() = runTest(testDispatcher) {
        val repository = mockk<RocketsRepository> {
            coEvery { getRockets() } returns flow { throw Exception() }
        }
        val viewModel = rocketListViewModel(repository)

        advanceUntilIdle()
        viewModel.uiState.value shouldBe UiScreenState.Error(UiText.StringResource(R.string.unknown_error))
    }

    @Test
    fun `uiState should be error on MalformedJsonException`() = runTest(testDispatcher) {
        val repository = mockk<RocketsRepository> {
            coEvery { getRockets() } returns flow { throw MalformedJsonException("") }
        }
        val viewModel = rocketListViewModel(repository)

        advanceUntilIdle()
        viewModel.uiState.value shouldBe UiScreenState.Error(UiText.StringResource(R.string.error_json))
    }

    @Test
    fun `uiState should be error on TimeoutException`() = runTest(testDispatcher) {
        val repository = mockk<RocketsRepository> {
            coEvery { getRockets() } returns flow { throw TimeoutException() }
        }
        val viewModel = rocketListViewModel(repository)

        advanceUntilIdle()
        viewModel.uiState.value shouldBe UiScreenState.Error(UiText.StringResource(R.string.error_timeout))
    }

    @Test
    fun `uiState should be error on IOException`() = runTest(testDispatcher) {
        val repository = mockk<RocketsRepository> {
            coEvery { getRockets() } returns flow { throw IOException() }
        }
        val viewModel = rocketListViewModel(repository)

        advanceUntilIdle()
        viewModel.uiState.value shouldBe UiScreenState.Error(UiText.StringResource(R.string.error_io))
    }

    @Test
    fun `uiState should be error on HttpException`() = runTest(testDispatcher) {
        val repository = mockk<RocketsRepository> {
            coEvery { getRockets() } returns flow { throw HttpException("400", null) }
        }
        val viewModel = rocketListViewModel(repository)

        advanceUntilIdle()
        viewModel.uiState.value shouldBe UiScreenState.Error(UiText.StringResource(R.string.error_server_response))
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
        viewModel.uiState.value shouldBe UiScreenState.Data(emptyList())

        viewModel.fetchRockets()
        advanceUntilIdle()
        viewModel.uiState.value shouldBe UiScreenState.Data(rocketsData.map { it.asRocket() })
    }

    private fun rocketListViewModel(repository: RocketsRepository): RocketListViewModel {
        val viewModel = RocketListViewModel(repository)
        viewModel.initialize()
        return viewModel
    }
}
