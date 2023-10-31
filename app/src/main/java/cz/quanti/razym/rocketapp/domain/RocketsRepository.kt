package cz.quanti.razym.rocketapp.domain

import cz.quanti.razym.rocketapp.model.Rocket
import cz.quanti.razym.rocketapp.presentation.Resource
import kotlinx.coroutines.flow.Flow

interface RocketsRepository {
    fun getRockets(): Flow<List<Rocket>>
}