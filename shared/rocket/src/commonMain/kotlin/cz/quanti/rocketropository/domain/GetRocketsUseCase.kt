package cz.quanti.rocketropository.domain

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import cz.quanti.rocketropository.Result
import cz.quanti.rocketropository.model.Rocket
import kotlinx.coroutines.flow.Flow

// TODO: separate classes for Unit param UseCases, or extensions?
//fun interface GetRocketsUseCase : FlowUseCaseUnit<List<RocketData>> {
fun interface GetRocketsUseCase : FlowUseCase<Unit, Result<List<Rocket>>> {
    @NativeCoroutines
    override operator fun invoke(input: Unit): Flow<Result<List<Rocket>>>
}
