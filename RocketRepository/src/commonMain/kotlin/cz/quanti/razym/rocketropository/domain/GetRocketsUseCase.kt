package cz.quanti.razym.rocketropository.domain

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import cz.quanti.razym.rocketropository.data.RocketData
import kotlinx.coroutines.flow.Flow

// TODO: separate classes for Unit param UseCases, or extensions?
//fun interface GetRocketsUseCase : FlowUseCaseUnit<List<RocketData>> {
fun interface GetRocketsUseCase : FlowUseCase<Unit, List<RocketData>> {
    @NativeCoroutines
    override operator fun invoke(input: Unit): Flow<List<RocketData>>
}
