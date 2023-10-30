package cz.quanti.razym.rocketapp.domain

interface RocketsRepo {
    fun getRockets(): List<RocketModel>
}