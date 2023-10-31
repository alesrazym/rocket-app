package cz.quanti.razym.rocketapp.domain

interface RocketsRepository {
    suspend fun getRockets(): List<RocketModel>
}