package cz.quanti.rocketropository.domain

import cz.quanti.rocketropository.Result
import cz.quanti.rocketropository.asResult
import cz.quanti.rocketropository.data.RocketData
import cz.quanti.rocketropository.data.StageData
import cz.quanti.rocketropository.model.RocketDetail
import cz.quanti.rocketropository.model.Stage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetRocketUseCaseImpl(
    private val repo: RocketsRepository
) : GetRocketUseCase {
    override fun invoke(input: String): Flow<Result<RocketDetail>> {
        return repo.getRocket(input).map { data ->
            data.asRocketDetail()
        }.asResult()
    }
}

fun RocketData.asRocketDetail(): RocketDetail {
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

fun StageData.asStage(): Stage {
    return Stage(this.burnTimeSec, this.engines, this.fuelAmountTons, this.reusable)
}

