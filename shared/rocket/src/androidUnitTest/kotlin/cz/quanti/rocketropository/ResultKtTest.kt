package cz.quanti.rocketropository

import cz.quanti.common.Result
import cz.quanti.common.RocketException
import cz.quanti.common.asResult
import io.kotest.matchers.shouldBe
import io.ktor.client.engine.ClientEngineClosedException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.SendCountExceedException
import io.ktor.client.plugins.contentnegotiation.ContentConverterException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.errors.IOException
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest

class ResultKtTest {
    @Test
    fun `asResult emits Loading at start then Success on valid data`() =
        runTest {
            val flow = flow { emit("valid data") }
            val resultFlow = flow.asResult()

            // Multiple ways to get values from flow.
            // 1. toList().
            val results = resultFlow.toList()

            results[0] shouldBe Result.Loading
            results[1] shouldBe Result.Success("valid data")

            // 2. separate collect {} to only some elements using drop and take.
            resultFlow.take(1).collect { result ->
                result shouldBe Result.Loading
            }
            resultFlow.drop(1).take(1).collect { result ->
                result shouldBe Result.Success("valid data")
            }
        }

    // TODO can we use parametrized test case? How will we test in KMP?
    @Test
    fun `asResult emits Error on HttpRequestTimeoutException`() =
        runTest {
            val exception = HttpRequestTimeoutException("url", 1000L)
            exception shouldResult
                RocketException.NetworkException(exception.message!!, exception)
        }

    @Test
    fun `asResult emits Error on IOException`() =
        runTest {
            val exception = IOException("message")
            exception shouldResult
                RocketException.NetworkException(exception.message!!, exception)
        }

    @Test
    fun `asResult emits Error on ResponseException`() =
        runTest {
            val httpResponse = mockk<HttpResponse>()
            every { httpResponse.status } returns HttpStatusCode.NotFound
            val exception = ResponseException(httpResponse, "cache")
            exception shouldResult
                RocketException.HttpException(HttpStatusCode.NotFound, exception)
        }

    @Test
    fun `asResult emits Error on SendCountExceedException`() =
        runTest {
            val exception = SendCountExceedException("message")
            exception shouldResult
                RocketException.NetworkException(exception.message!!, exception)
        }

    @Test
    fun `asResult emits Error on ClientEngineClosedException`() =
        runTest {
            val exception = ClientEngineClosedException(null)
            exception shouldResult
                RocketException.Exception(exception.message!!, exception)
        }

    @Test
    fun `asResult emits Error on ContentConverterException`() =
        runTest {
            val exception = ContentConverterException("Bad content")
            exception shouldResult
                RocketException.ContentException(exception.message!!, exception)
        }

    @Test
    fun `asResult emits Error on other Exception`() =
        runTest {
            val exception = IllegalArgumentException("Bad content")
            exception shouldResult
                RocketException.Exception(exception.message!!, exception)
        }

    private suspend infix fun Throwable.shouldResult(expectedException: RocketException) {
        val flow = flow<String> { throw this@shouldResult }
        val resultFlow = flow.asResult()

        resultFlow.drop(1)
            .take(1)
            .collect { result ->
                result shouldBe Result.Error(expectedException)
            }
    }
}
