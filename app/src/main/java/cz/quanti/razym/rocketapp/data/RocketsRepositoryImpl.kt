package cz.quanti.razym.rocketapp.data

import cz.quanti.razym.rocketapp.domain.RocketsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RocketsRepositoryImpl(
    private val api: SpaceXApi
) : RocketsRepository {
    override fun getRockets(): Flow<List<RocketData>> = flow {
        emit(api.listRockets())
    }
}