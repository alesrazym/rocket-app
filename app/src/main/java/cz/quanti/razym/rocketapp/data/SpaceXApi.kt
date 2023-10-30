package cz.quanti.razym.rocketapp.data

import retrofit2.http.GET

interface SpaceXApi {
    @GET("v4/rockets")
    suspend fun listRockets(): List<RocketData>
}