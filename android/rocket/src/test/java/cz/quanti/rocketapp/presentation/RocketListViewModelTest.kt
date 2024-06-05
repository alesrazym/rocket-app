package cz.quanti.rocketapp.presentation

import android.icu.text.DateFormat
import cz.quanti.common.Result
import cz.quanti.common.ResultException
import cz.quanti.common.asResult
import cz.quanti.rocketapp.android.rocket.R
import cz.quanti.rocketapp.util.toLocalString
import cz.quanti.rocketapp.util.rocketsData
import cz.quanti.rocketropository.domain.GetRocketsUseCase
import cz.quanti.rocketropository.model.Rocket
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
    private val rocketsData: List<Rocket> = rocketsData()

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
    fun `should convert model to uiState`() {
        val data = rocketsData.first { it.id == "5e9d0d95eda69955f709d1eb" }

        val model = data.asRocketUiState()

        model.id shouldBe "5e9d0d95eda69955f709d1eb"
        model.name shouldBe UiText.DynamicString("Falcon 1")
        model.firstFlight shouldBe UiText.StringResource(
            R.string.first_flight,
            "Mar 24, 2006",
        )
    }

    @Test
    fun `uiState should be loading first`() = runTest(testDispatcher) {
        val useCase = GetRocketsUseCase {
            flow {
                delay(1000)
                emit(emptyList<Rocket>())
            }.asResult()
        }

        val viewModel = rocketListViewModel(useCase)

        // Loading state until we let the coroutine in model work with advanceUntilIdle()
        advanceTimeBy(1)
        viewModel.uiState.value is UiScreenState.Loading

        advanceUntilIdle()
        viewModel.uiState.value shouldBe UiScreenState.Data(emptyList())
    }

    @Test
    fun `uiState should be success`() = runTest(testDispatcher) {
        val useCase = GetRocketsUseCase {
            flow {
                emit(rocketsData)
            }.asResult()
        }

        val viewModel = rocketListViewModel(useCase)

        advanceUntilIdle()
        viewModel.uiState.value shouldBe UiScreenState.Data(
            rocketsData.map { it.asRocketUiState() })
    }

    @Test
    fun `uiState should be updated after fetch`() = runTest(testDispatcher) {
        val useCase = mockk<GetRocketsUseCase>()
        var callCount = 0

        coEvery { useCase(Unit) } answers {
            callCount++
            when (callCount) {
                1 -> flow { emit(emptyList<Rocket>()) }.asResult()
                else -> flow { emit(rocketsData) }.asResult()
            }
        }

        val viewModel = rocketListViewModel(useCase)

        viewModel.uiState.value shouldBe UiScreenState.Loading(UiText.StringResource(R.string.rockets_loading))

        advanceUntilIdle()
        viewModel.uiState.value shouldBe UiScreenState.Data(emptyList())

        viewModel.fetchRockets()
        advanceUntilIdle()
        viewModel.uiState.value shouldBe
            UiScreenState.Data(rocketsData.map { it.asRocketUiState() })
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
