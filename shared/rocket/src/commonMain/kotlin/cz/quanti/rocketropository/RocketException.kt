package cz.quanti.rocketropository

import io.ktor.http.HttpStatusCode

sealed class RocketException(message: String, cause: Throwable?) : Throwable(message, cause) {
    class HttpException(
        val statusCode: HttpStatusCode,
        cause: Throwable?,
    ) : RocketException("HTTP error: $statusCode", cause)

    class NetworkException(val errorMessage: String, cause: Throwable?) : RocketException(errorMessage, cause)

    class ContentException(val errorMessage: String, cause: Throwable?) : RocketException(errorMessage, cause)

    class Exception(val errorMessage: String, cause: Throwable?) : RocketException(errorMessage, cause)
}
