package cz.quanti.razym.rocketapp.domain

interface RocketsRepository {
    fun getRockets(): List<RocketModel>
}