package cz.quanti.razym.rocketapp.data

import cz.quanti.razym.rocketapp.domain.RocketsRepository
import cz.quanti.razym.rocketapp.model.RocketModel

class RocketsRepositoryImpl : RocketsRepository {
    override fun getRockets(): List<RocketModel> {
        return listOf(
            RocketModel("Falcon 1", "First flight: 2006-03-24"),
            RocketModel("Falcon 9", "First flight: 2010-06-04"),
            RocketModel("Falcon Heavy", "First flight: 2018-02-06"),
            RocketModel("Starship", "First flight: 2021-12-31")
        )
    }
}