package cz.quanti.razym.rocketapp.domain

import cz.quanti.razym.rocketapp.data.RocketData
import kotlinx.coroutines.flow.Flow

interface RocketsRepository {
    fun getRockets(): Flow<List<RocketData>>
    fun getRocket(id: String): Flow<RocketData>
}