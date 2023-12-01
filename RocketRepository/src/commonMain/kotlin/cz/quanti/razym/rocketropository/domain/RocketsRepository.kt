package cz.quanti.razym.rocketropository.domain

import cz.quanti.razym.rocketropository.data.RocketData
import kotlinx.coroutines.flow.Flow

interface RocketsRepository {
    fun getRockets(): Flow<List<RocketData>>
    fun getRocket(id: String): Flow<RocketData>
}