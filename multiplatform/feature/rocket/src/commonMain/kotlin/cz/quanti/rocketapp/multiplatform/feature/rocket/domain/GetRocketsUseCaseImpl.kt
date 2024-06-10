package cz.quanti.rocketapp.multiplatform.feature.rocket.domain

import cz.quanti.rocketapp.multiplatform.feature.rocket.data.RocketData
import cz.quanti.rocketapp.multiplatform.feature.rocket.model.Rocket
import cz.quanti.rocketapp.multiplatform.lib.common.infrastructure.asResult
import cz.quanti.rocketapp.multiplatform.lib.common.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class GetRocketsUseCaseImpl(
    private val repo: RocketsRepository
) : GetRocketsUseCase {
    override fun invoke(input: Unit): Flow<Result<List<Rocket>>> {
        return repo.getRockets()
            .map { list ->
                list.map { it.asRocket() }
            }
            .asResult()
    }
}

internal fun RocketData.asRocket(): Rocket {
    return Rocket(
        name,
        firstFlight,
        id,
    )
}
