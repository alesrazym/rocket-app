package cz.quanti.razym.rocketapp.data

import cz.quanti.razym.rocketapp.domain.RocketsRepository
import cz.quanti.razym.rocketapp.model.Rocket

class RocketsRepositoryImpl : RocketsRepository {
    override fun getRockets(): List<Rocket> {
        return listOf(
            Rocket("Falcon 1", "First flight: 2006-03-24"),
            Rocket("Falcon 9", "First flight: 2010-06-04"),
            Rocket("Falcon Heavy", "First flight: 2018-02-06"),
            Rocket("Starship", "First flight: 2021-12-31")
        )
    }
}