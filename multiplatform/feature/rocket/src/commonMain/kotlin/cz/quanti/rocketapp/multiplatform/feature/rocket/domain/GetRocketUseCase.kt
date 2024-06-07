package cz.quanti.rocketapp.multiplatform.feature.rocket.domain

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import cz.quanti.rocketapp.multiplatform.feature.rocket.model.RocketDetail
import cz.quanti.rocketapp.multiplatform.lib.common.Result
import cz.quanti.rocketapp.multiplatform.lib.common.domain.FlowUseCase
import kotlinx.coroutines.flow.Flow

fun interface GetRocketUseCase : FlowUseCase<String, Result<RocketDetail>> {
    @NativeCoroutines
    override operator fun invoke(input: String): Flow<Result<RocketDetail>>
}
