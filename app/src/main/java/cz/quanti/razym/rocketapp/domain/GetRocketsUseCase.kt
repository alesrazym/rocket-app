package cz.quanti.razym.rocketapp.domain

import cz.quanti.razym.rocketapp.data.RocketData
import kotlinx.coroutines.flow.Flow

interface GetRocketsUseCase {
    operator fun invoke(): Flow<List<RocketData>>
}

