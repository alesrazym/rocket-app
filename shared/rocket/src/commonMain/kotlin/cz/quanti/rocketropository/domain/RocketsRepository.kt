package cz.quanti.rocketropository.domain

import cz.quanti.rocketropository.data.RocketData
import kotlinx.coroutines.flow.Flow

interface RocketsRepository {
    fun getRockets(): Flow<List<RocketData>>

    fun getRocket(id: String): Flow<RocketData>
}
