package cz.quanti.rocketapp.multiplatform.feature.rocket.domain

import cz.quanti.rocketapp.multiplatform.feature.rocket.model.RocketData
import cz.quanti.rocketapp.multiplatform.feature.rocket.model.RocketDetail
import cz.quanti.rocketapp.multiplatform.feature.rocket.model.Stage
import cz.quanti.rocketapp.multiplatform.feature.rocket.model.StageData
import cz.quanti.rocketapp.multiplatform.lib.common.infrastructure.asResult
import cz.quanti.rocketapp.multiplatform.lib.common.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class GetRocketUseCaseImpl(
    private val repo: RocketsRepository
) : GetRocketUseCase {
    override fun invoke(input: String): Flow<Result<RocketDetail>> {
        return repo.getRocket(input)
            .map { data ->
                data.asRocketDetail()
            }
            .asResult()
    }
}

internal fun RocketData.asRocketDetail(): RocketDetail {
    return RocketDetail(
        this.name,
        this.id,
        this.overview,
        this.height[RocketData.METERS] ?: 0.0,
        this.diameter[RocketData.METERS] ?: 0.0,
        (this.mass[RocketData.KG] ?: 0.0) / 1000.0,
        this.firstStage.asStage(),
        this.secondStage.asStage(),
        this.flickrImages,
    )
}

internal fun StageData.asStage(): Stage {
    return Stage(this.burnTimeSec, this.engines, this.fuelAmountTons, this.reusable)
}

