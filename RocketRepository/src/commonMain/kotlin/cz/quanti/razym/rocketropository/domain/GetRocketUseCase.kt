package cz.quanti.razym.rocketropository.domain

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import cz.quanti.razym.rocketropository.data.RocketData
import kotlinx.coroutines.flow.Flow

fun interface GetRocketUseCase : FlowUseCase<String, RocketData> {
    @NativeCoroutines
    override operator fun invoke(input: String): Flow<RocketData>
}
