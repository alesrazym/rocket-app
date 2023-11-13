package cz.quanti.razym.rocketapp.domain

import cz.quanti.razym.rocketapp.data.RocketData
import kotlinx.coroutines.flow.Flow

class GetRocketsUseCaseImpl(
    private val repo: RocketsRepository
) : GetRocketsUseCase {
    override operator fun invoke(): Flow<List<RocketData>> {
        return repo.getRockets()
    }
}