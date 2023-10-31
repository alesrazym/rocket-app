package cz.quanti.razym.rocketapp.data

import cz.quanti.razym.rocketapp.domain.RocketModel
import cz.quanti.razym.rocketapp.domain.RocketsRepository

class RocketsRepositoryImpl(
    private val api: SpaceXApi
) : RocketsRepository {
    override suspend fun getRockets(): List<RocketModel> {
        return try {
            val list = api.listRockets()

            return list.map { RocketModel(it.name, "First flight: ${it.first_flight}") }
        } catch (e: Exception) {
            // TODO: better error handling with status, i.e. Resource<T> as per
            // https://github.com/android/architecture-components-samples/blob/master/GithubBrowserSample/app/src/main/java/com/android/example/github/vo/Resource.kt
            emptyList()
        }
    }
}