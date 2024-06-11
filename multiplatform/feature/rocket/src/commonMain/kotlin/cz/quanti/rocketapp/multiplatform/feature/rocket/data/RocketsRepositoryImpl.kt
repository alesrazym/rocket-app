package cz.quanti.rocketapp.multiplatform.feature.rocket.data

import cz.quanti.rocketapp.multiplatform.feature.rocket.domain.RocketsRepository
import cz.quanti.rocketapp.multiplatform.feature.rocket.model.RocketData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class RocketsRepositoryImpl(
    private val api: SpaceXApi,
) : RocketsRepository {
    override fun getRockets(): Flow<List<RocketData>> =
        flow {
            emit(api.listRockets())
        }

    override fun getRocket(id: String): Flow<RocketData> =
        flow {
            emit(api.getRocket(id))
        }
}
