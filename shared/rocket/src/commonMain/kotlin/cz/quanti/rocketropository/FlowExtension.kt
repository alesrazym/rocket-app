package cz.quanti.rocketropository

import io.ktor.client.utils.unwrapCancellationException
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

@Suppress("detekt.SwallowedException")
fun <T> cancelExceptionThrowingFlow(block: suspend FlowCollector<T>.() -> Unit): Flow<T> =
    flow {
        try {
            block()
        } catch (e: CancellationException) {
            // Job.cancel() throws internal JobCancellationException,
            // that is consumed and not propagated from flow, i.e.
            // cannot be caught in Flow<T>.catch { ... } block.
            // But, it still cancels the caller coroutine.

            // On the other hand, Ktor on Android throws
            // java.util.concurrent.CancellationException when job cancelled,
            // which is propagated out of flow. In this case, caller coroutine
            // is NOT cancelled and continues!

            // There is no win-win solution, we have to carefully select based on usage.

            // Here, we rethrow JobCancellationException as new CancellationException
            // to propagate it from flow, but not to terminate the coroutine.
            // Coroutine is marked as not active, so it can be terminated by checking
            // `coroutineContext.ensureActive()` as a next call after cancellable statement.

            // More info:
            // https://betterprogramming.pub/the-silent-killer-thats-crashing-your-coroutines-9171d1e8f79b

            if (e::class.qualifiedName == "kotlinx.coroutines.JobCancellationException") {
                throw CancellationException(e.message, e.cause?.unwrapCancellationException())
            }

            throw e.unwrapCancellationException()
        }
    }
