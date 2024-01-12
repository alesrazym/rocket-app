package cz.quanti.rocketapp.presentation

import android.icu.text.DateFormat
import cz.quanti.rocketapp.android.rocket.R
import cz.quanti.rocketapp.util.toLocalString
import cz.quanti.rocketapp.utils.rocketsData
import cz.quanti.common.Result
import cz.quanti.common.ResultException
import cz.quanti.rocketropository.data.RocketData
import cz.quanti.rocketropository.domain.GetRocketsUseCase
import cz.quanti.rocketropository.domain.GetRocketsUseCaseImpl
import cz.quanti.rocketropository.domain.RocketsRepository
import cz.quanti.rocketropository.domain.asRocket
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Date
import java.util.Locale
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
        mockkStatic("cz.quanti.rocketapp.util.DateTimeUtilsKt")
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

        val model = data.asRocket().asRocketUiState()

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
        viewModel.uiState.value shouldBe UiScreenState.Data(rocketsData.map { it.asRocket().asRocketUiState() })
    }

    // TODO can we use parametrized test case? How will we test in KMP?
    @Test
    fun `uiState should be error on Exception`() {
        ResultException.Exception("error", null) shouldResult
            UiScreenState.Error(UiText.StringResource(R.string.unknown_error))
    }

    @Test
    fun `uiState should be error on ContentException`() {
        ResultException.ContentException("error", null) shouldResult
            UiScreenState.Error(UiText.StringResource(R.string.error_json))
    }

    @Test
    fun `uiState should be error on NetworkException`() {
        ResultException.NetworkException("error", null) shouldResult
            UiScreenState.Error(UiText.StringResource(R.string.error_io))
    }

    @Test
    fun `uiState should be error on HttpException`() {
        mockk<ResultException.HttpException>() shouldResult
            UiScreenState.Error(UiText.StringResource(R.string.error_server_response))
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
        viewModel.uiState.value shouldBe UiScreenState.Data(rocketsData.map { it.asRocket().asRocketUiState() })
    }

    private fun rocketListViewModel(repository: RocketsRepository): RocketListViewModel {
        val viewModel = RocketListViewModel(GetRocketsUseCaseImpl(repository))
        viewModel.initialize()
        return viewModel
    }

    private fun rocketListViewModel(useCase: GetRocketsUseCase): RocketListViewModel {
        val viewModel = RocketListViewModel(useCase)
        viewModel.initialize()
        return viewModel
    }

    private infix fun ResultException.shouldResult(expectedException: UiScreenState<*>) =
        runTest(testDispatcher) {
            val viewModel =
                rocketListViewModel {
                    flow { emit(Result.Error(this@shouldResult)) }
                }

            advanceUntilIdle()
            viewModel.uiState.value shouldBe expectedException
        }
}
