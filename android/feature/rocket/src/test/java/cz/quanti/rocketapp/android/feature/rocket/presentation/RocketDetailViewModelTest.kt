package cz.quanti.rocketapp.android.feature.rocket.presentation

import cz.quanti.rocketapp.android.feature.rocket.util.rocketsDetailData
import cz.quanti.rocketapp.android.lib.uisystem.presentation.UiScreenState
import cz.quanti.rocketapp.android.lib.uisystem.presentation.UiText
import cz.quanti.rocketapp.android.rocket.R
import cz.quanti.rocketapp.multiplatform.feature.rocket.domain.GetRocketUseCase
import cz.quanti.rocketapp.multiplatform.lib.common.asResult
import io.kotest.matchers.shouldBe
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class RocketDetailViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val validIds = listOf("5e9d0d95eda69955f709d1eb", "5e9d0d95eda69973a809d1ec")
    private val errorId = "non-existing-id"
    private val rocketsData = rocketsDetailData()
    private val useCase = GetRocketUseCase {
        flow {
            emit(rocketsData.first { rocket -> rocket.id == it })
        }.asResult()
    }

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState should be loading first`() = runTest(testDispatcher) {
        val id = validIds[0]

        val viewModel = rocketDetailViewModel(id)

        // Loading state until we let the coroutine in model work with advanceUntilIdle()
        advanceTimeBy(1)
        viewModel.uiState.value is UiScreenState.Loading

        advanceUntilIdle()
        viewModel.uiState.value shouldBe UiScreenState.Data(
            rocketsData[0].asRocketDetailUiState(),
        )
    }

    @Test
    fun `uiState should be success`() = runTest(testDispatcher) {
        val id = validIds[0]

        val viewModel = rocketDetailViewModel(id)

        advanceUntilIdle()
        assertSuccess(viewModel, id)
    }

    @Test
    fun `uiState should be error`() = runTest(testDispatcher) {
        val viewModel = rocketDetailViewModel(errorId)

        advanceUntilIdle()
        viewModel.uiState.value shouldBe UiScreenState.Error(UiText.StringResource(R.string.unknown_error))
    }

    @Test
    fun `uiState should be updated after fetch`() = runTest(testDispatcher) {
        val viewModel = rocketDetailViewModel(validIds[0])

        advanceUntilIdle()
        assertSuccess(viewModel, rocketsData[0].id)

        viewModel.fetchRocket(validIds[1])
        advanceUntilIdle()
        assertSuccess(viewModel, rocketsData[1].id)
    }

    private fun rocketDetailViewModel(id: String): RocketDetailViewModel {
        val viewModel = RocketDetailViewModel(useCase)
        viewModel.initialize(id)
        return viewModel
    }

    private fun assertSuccess(
        viewModel: RocketDetailViewModel,
        id: String,
    ) {
        val value = viewModel.uiState.value as UiScreenState.Data<RocketDetailUiState>
        value.refreshing shouldBe false
        value.data.id shouldBe id
    }
}
