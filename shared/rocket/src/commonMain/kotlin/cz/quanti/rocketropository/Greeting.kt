package cz.quanti.rocketropository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

class Greeting {
    private val platform: Platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }

    private val client = HttpClient()

    suspend fun greeting(): String {
        val response = client.get("https://ktor.io/docs/")
        return response.bodyAsText()
    }

}
