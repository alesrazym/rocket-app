package cz.quanti.razym.rocketapp.data

import cz.quanti.razym.rocketapp.domain.RocketModel
import cz.quanti.razym.rocketapp.domain.RocketsRepository
import cz.quanti.razym.rocketapp.presentation.Resource

class RocketsRepositoryImpl(
    private val api: SpaceXApi
) : RocketsRepository {
    override suspend fun getRockets(): Resource<List<RocketModel>> {
        return try {
            val list = api.listRockets()

            return Resource.success(
                list.map { RocketModel(it.name, "First flight: ${it.first_flight}") }
            )
        } catch (e: Exception) {
            Resource.error(e.message ?: "Unknown error", null)
        }
    }
}