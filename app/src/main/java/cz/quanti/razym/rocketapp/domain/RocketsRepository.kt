package cz.quanti.razym.rocketapp.domain

import cz.quanti.razym.rocketapp.model.RocketModel

interface RocketsRepository {
    fun getRockets(): List<RocketModel>
}