package cz.quanti.common

import io.ktor.http.HttpStatusCode

sealed class ResultException(message: String, cause: Throwable?) : Throwable(message, cause) {
    class HttpException(
        statusCode: HttpStatusCode,
        cause: Throwable?,
    ) : ResultException("HTTP error: $statusCode", cause)

    class NetworkException(errorMessage: String, cause: Throwable?) : ResultException(errorMessage, cause)

    class ContentException(errorMessage: String, cause: Throwable?) : ResultException(errorMessage, cause)

    class CanceledByUserException(errorMessage: String) : ResultException(errorMessage, null)

    class Exception(errorMessage: String, cause: Throwable?) : ResultException(errorMessage, cause)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ResultException

        if (message != other.message) return false
        if (cause != other.cause) return false
        return true
    }

    override fun hashCode(): Int {
        return this::class.hashCode()
    }
}
