package cz.quanti.razym.rocketapp.data

import cz.quanti.razym.rocketapp.domain.RocketsRepository
import cz.quanti.razym.rocketapp.presentation.Resource
import cz.quanti.razym.rocketapp.model.Rocket

class RocketsRepositoryImpl(
    private val api: SpaceXApi
) : RocketsRepository {
    override suspend fun getRockets(): Resource<List<Rocket>> {
        return try {
            val list = api.listRockets()

            return Resource.success(
                list.map { Rocket(it.name, "First flight: ${it.first_flight}") }
            )
        } catch (e: Exception) {
            Resource.error(e.message ?: "Unknown error", null)
        }
    }
}