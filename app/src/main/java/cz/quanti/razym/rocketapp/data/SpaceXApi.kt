package cz.quanti.razym.rocketapp.data

import retrofit2.http.GET
import retrofit2.http.Path

interface SpaceXApi {
    @GET("v4/rockets")
    suspend fun listRockets(): List<RocketData>

    @GET("v4/rockets/{id}")
    suspend fun getRocket(
        @Path("id") id: String,
    ): RocketData
}
