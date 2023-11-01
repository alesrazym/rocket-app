package cz.quanti.razym.rocketapp.domain

import cz.quanti.razym.rocketapp.model.Rocket

interface RocketsRepository {
    fun getRockets(): List<Rocket>
}