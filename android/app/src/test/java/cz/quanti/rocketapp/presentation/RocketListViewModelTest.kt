package cz.quanti.rocketapp.presentation

import android.icu.text.DateFormat
import android.net.http.HttpException
import android.util.MalformedJsonException
import cz.quanti.rocketapp.R
import cz.quanti.rocketapp.util.toLocalString
import cz.quanti.rocketapp.utils.rocketsData
import cz.quanti.rocketropository.data.RocketData
import cz.quanti.rocketropository.domain.RocketsRepository
import cz.quanti.rocketropository.model.asRocket
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.util.Date
import java.util.Locale
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

        // TODO: Possible solution for mocking android methods -> replace by java equivalent
        mockkStatic("cz.quanti.razym.rocketapp.util.DateTimeUtilsKt")
        every { any<Date>().toLocalString() } answers {
            val date = firstArg<Date>()
            java.text.DateFormat.getDateInstance(
                DateFormat.MEDIUM,
                Locale.US,
            ).format(date)
        }
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `should convert data to model`() {
        val data = rocketsData.first { it.id == "5e9d0d95eda69955f709d1eb" }

        val model = data.asRocket()

        model.id shouldBe "5e9d0d95eda69955f709d1eb"
        model.name shouldBe UiText.DynamicString("Falcon 1")
        model.firstFlight shouldBe UiText.StringResource(
            R.string.first_flight,
            "Mar 24, 2006",
        )
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
        val viewModel = RocketListViewModel { repository.getRockets() }
        viewModel.initialize()
        return viewModel
    }
}
