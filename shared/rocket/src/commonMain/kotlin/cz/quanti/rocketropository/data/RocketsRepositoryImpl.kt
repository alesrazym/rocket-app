package cz.quanti.rocketropository.data

import cz.quanti.rocketropository.cancelExceptionThrowingFlow
import cz.quanti.rocketropository.domain.RocketsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class RocketsRepositoryImpl(
    private val api: SpaceXApi,
) : RocketsRepository {
    override fun getRockets(): Flow<List<RocketData>> =
        cancelExceptionThrowingFlow {
            // TODO: remove delay before merge
//            delay(2000)
            emit(api.listRockets())
        }

    override suspend fun getRocketsSuspend(): List<RocketData> {
        // TODO: remove delay before merge
//        delay(2000)
        return api.listRockets()
    }

    override fun getRocket(id: String): Flow<RocketData> =
        flow {
            emit(api.getRocket(id))
        }
}
