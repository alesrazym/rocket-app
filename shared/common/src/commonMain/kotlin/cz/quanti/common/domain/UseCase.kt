package cz.quanti.common.domain

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.coroutines.flow.Flow

// TODO should use case:
//  * return data or throw,
//  * or wrap it into Result?
//  If it should return Result, let's rewrite to
//  sealed interface UseCase<in I, out Result<O>>

sealed interface UseCase<in I, out O>

interface SynchronousUseCase<in I, out O> : UseCase<I, O> {
    operator fun invoke(input: I): O
}

interface SuspendUseCase<in I, out O> : UseCase<I, O> {
    @NativeCoroutines
    suspend operator fun invoke(input: I): O
}

interface FlowUseCase<in I, out O> : UseCase<I, O> {
    @NativeCoroutines
    operator fun invoke(input: I): Flow<O>
}

// TODO: separate classes for Unit param UseCases, or extensions?
//  Extension needs additional import where used,
//  which makes it little bit less friendly then expected.
/*
interface SynchronousUseCaseUnit<out O> : UseCase<Unit, O> {
    operator fun invoke(): O
}

interface SuspendUseCaseUnit<out O> : UseCase<Unit, O> {
    @NativeCoroutines
    suspend operator fun invoke(): O
}

interface FlowUseCaseUnit<out O> : UseCase<Unit, O> {
    @NativeCoroutines
    operator fun invoke(): Flow<O>
}
*/

operator fun <O> SynchronousUseCase<Unit, O>.invoke(): O = invoke(Unit)

suspend operator fun <O> SuspendUseCase<Unit, O>.invoke(): O = invoke(Unit)

operator fun <O> FlowUseCase<Unit, O>.invoke(): Flow<O> = invoke(Unit)
