package cz.quanti.rocketapp.multiplatform.feature.rocket.domain

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import cz.quanti.rocketapp.multiplatform.feature.rocket.model.Rocket
import cz.quanti.rocketapp.multiplatform.lib.common.Result
import cz.quanti.rocketapp.multiplatform.lib.common.domain.FlowUseCase
import kotlinx.coroutines.flow.Flow

// TODO: separate classes for Unit param UseCases, or extensions?
//fun interface GetRocketsUseCase : FlowUseCaseUnit<List<RocketData>> {
fun interface GetRocketsUseCase : FlowUseCase<Unit, Result<List<Rocket>>> {
    @NativeCoroutines
    override operator fun invoke(input: Unit): Flow<Result<List<Rocket>>>
}
