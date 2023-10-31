package cz.quanti.razym.rocketapp.data

import cz.quanti.razym.rocketapp.domain.RocketsRepository
import cz.quanti.razym.rocketapp.presentation.Resource
import cz.quanti.razym.rocketapp.model.Rocket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class RocketsRepositoryImpl(
    private val api: SpaceXApi
) : RocketsRepository {
    override fun getRockets(): Flow<List<Rocket>> = flow {
//        return try {
            val list = api.listRockets()

            val model = list.map {
                Rocket(it.name, "First flight: ${it.first_flight}")
            }

            emit(model)
//        } catch (e: Exception) {
//            Resource.error(e.message ?: "Unknown error", null)
//        }
    }
}