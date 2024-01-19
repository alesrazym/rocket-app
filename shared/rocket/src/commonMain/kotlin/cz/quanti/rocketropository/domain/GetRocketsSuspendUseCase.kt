package cz.quanti.rocketropository.domain

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import cz.quanti.common.Result
import cz.quanti.common.domain.SuspendUseCase
import cz.quanti.rocketropository.model.Rocket

fun interface GetRocketsSuspendUseCase : SuspendUseCase<Unit, Result<List<Rocket>>> {
    @NativeCoroutines
    override suspend operator fun invoke(input: Unit): Result<List<Rocket>>
}
