package cz.quanti.common

import io.ktor.http.HttpStatusCode

sealed class RocketException(message: String, cause: Throwable?) : Throwable(message, cause) {
    class HttpException(
        statusCode: HttpStatusCode,
        cause: Throwable?,
    ) : RocketException("HTTP error: $statusCode", cause)

    class NetworkException(errorMessage: String, cause: Throwable?) : RocketException(errorMessage, cause)

    class ContentException(errorMessage: String, cause: Throwable?) : RocketException(errorMessage, cause)

    class Exception(errorMessage: String, cause: Throwable?) : RocketException(errorMessage, cause)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as RocketException

        if (message != other.message) return false
        if (cause != other.cause) return false
        return true
    }

    override fun hashCode(): Int {
        return this::class.hashCode()
    }
}
