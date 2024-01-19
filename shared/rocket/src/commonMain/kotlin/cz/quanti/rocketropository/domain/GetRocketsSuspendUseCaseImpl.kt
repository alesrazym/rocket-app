package cz.quanti.rocketropository.domain

import cz.quanti.common.Result
import cz.quanti.common.asResult
import cz.quanti.rocketropository.model.Rocket

internal class GetRocketsSuspendUseCaseImpl(
    private val repo: RocketsRepository
) : GetRocketsSuspendUseCase {
    override suspend fun invoke(input: Unit): Result<List<Rocket>> {
        return asResult { repo.getRocketsSuspend().map { it.asRocket() } }
    }
}
