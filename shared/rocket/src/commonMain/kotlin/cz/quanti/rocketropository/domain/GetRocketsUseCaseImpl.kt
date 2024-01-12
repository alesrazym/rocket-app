package cz.quanti.rocketropository.domain

import cz.quanti.common.Result
import cz.quanti.common.asResult
import cz.quanti.rocketropository.data.RocketData
import cz.quanti.rocketropository.model.Rocket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetRocketsUseCaseImpl(
    private val repo: RocketsRepository
) : GetRocketsUseCase {
    override fun invoke(input: Unit): Flow<Result<List<Rocket>>> {
        return repo.getRockets().map { list ->
            list.map { it.asRocket() }
        }.asResult()
    }
}

fun RocketData.asRocket(): Rocket {
    return Rocket(
        name,
        firstFlight,
        id,
    )
}
