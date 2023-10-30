package cz.quanti.razym.rocketapp.domain

import cz.quanti.razym.rocketapp.model.Rocket
import cz.quanti.razym.rocketapp.presentation.Resource

interface RocketsRepository {
    suspend fun getRockets(): Resource<List<Rocket>>
}