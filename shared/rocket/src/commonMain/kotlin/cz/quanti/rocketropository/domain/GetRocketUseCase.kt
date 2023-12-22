package cz.quanti.rocketropository.domain

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import cz.quanti.rocketropository.Result
import cz.quanti.rocketropository.model.RocketDetail
import kotlinx.coroutines.flow.Flow

fun interface GetRocketUseCase : FlowUseCase<String, Result<RocketDetail>> {
    @NativeCoroutines
    override operator fun invoke(input: String): Flow<Result<RocketDetail>>
}
