package cz.quanti.rocketapp.multiplatform.feature.rocket.domain

import cz.quanti.rocketapp.multiplatform.feature.rocket.data.RocketData
import kotlinx.coroutines.flow.Flow

internal interface RocketsRepository {
    fun getRockets(): Flow<List<RocketData>>

    fun getRocket(id: String): Flow<RocketData>
}
