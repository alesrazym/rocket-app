package cz.quanti.razym.rocketapp.presentation

import com.squareup.moshi.Types
import cz.quanti.razym.rocketapp.data.RocketData
import cz.quanti.razym.rocketapp.domain.RocketsRepository
import cz.quanti.razym.rocketapp.presentation.RocketDetailViewModel.UiState
import cz.quanti.razym.rocketapp.utils.TestUtils
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldStartWith
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RocketDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val validIds = listOf("5e9d0d95eda69955f709d1eb", "5e9d0d95eda69973a809d1ec")
    private val errorId = "non-existing-id"
    private val rocketsData = TestUtils.loadJsonResource<List<RocketData>>(
        "rockets.json",
        Types.newParameterizedType(List::class.java, RocketData::class.java),
    )
    private val repository = createRepository()

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
        val data = rocketsData.first { it.id == validIds[0] }

        val model = data.asRocketDetail()

        model.id shouldBe validIds[0]
        model.name shouldBe "Falcon 1"
        model.overview shouldStartWith "The Falcon 1 was an"
        model.heightMeters shouldBe 22.25
        model.diameterMeters shouldBe 1.68
        model.massTons shouldBe 30.146
        model.firstStage.burnTimeSec shouldBe 169
        model.firstStage.engines shouldBe 1
        model.firstStage.fuelAmountTons shouldBe 44.3
        model.firstStage.reusable shouldBe false
        model.secondStage.burnTimeSec shouldBe 378
        model.secondStage.engines shouldBe 1
        model.secondStage.fuelAmountTons shouldBe 3.38
        model.secondStage.reusable shouldBe false
        model.flickrImages.size shouldBe 2
    }

    @Test
    fun `uiState should be loading first`() = runTest(testDispatcher) {
        // repository is mocked
        val id = validIds[0]

        val viewModel = RocketDetailViewModel(repository)
        viewModel.fetchRocket(id)

        // Loading state until we let the coroutine in model work with advanceUntilIdle()
        viewModel.uiState.value.state shouldBe UiState.Loading

        advanceUntilIdle()
        viewModel.uiState.value.state shouldNotBe UiState.Loading
    }

    @Test
    fun `uiState should be success`() = runTest(testDispatcher) {
        // repository is mocked
        val id = validIds[0]

        val viewModel = RocketDetailViewModel(repository)
        viewModel.fetchRocket(id)

        advanceUntilIdle()
        assertSuccess(viewModel, id)
    }

    @Test
    fun `uiState should be error`() = runTest(testDispatcher) {
        // repository is mocked

        val viewModel = RocketDetailViewModel(repository)
        viewModel.fetchRocket(errorId)

        advanceUntilIdle()
        shouldNotThrowAny {
            viewModel.uiState.value.state as UiState.Error
        }
    }

    @Test
    fun `uiState should be updated after fetch`() = runTest(testDispatcher) {
        // repository is mocked

        val viewModel = RocketDetailViewModel(repository)

        viewModel.fetchRocket(validIds[0])
        advanceUntilIdle()
        assertSuccess(viewModel, rocketsData[0].id)

        viewModel.fetchRocket(validIds[1])
        advanceUntilIdle()
        assertSuccess(viewModel, rocketsData[1].id)
    }

    private fun createRepository(): RocketsRepository = mockk {
        coEvery { getRocket(validIds[0]) } returns flowOf(rocketsData[0])
        coEvery { getRocket(validIds[1]) } returns flowOf(rocketsData[1])
        coEvery { getRocket(errorId) } returns flow { throw Exception() }
    }

    private fun assertSuccess(
        viewModel: RocketDetailViewModel,
        id: String,
    ) {
        val success = shouldNotThrowAny {
            viewModel.uiState.value.state as UiState.Success
        }
        success.rocket.id shouldBe id
    }
}
