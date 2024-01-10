package cz.quanti.rocketropository.domain

import cz.quanti.rocketropository.Result
import cz.quanti.rocketropository.asResult
import cz.quanti.rocketropository.model.Rocket

class GetRocketsSuspendUseCaseImpl(
    private val repo: RocketsRepository
) : GetRocketsSuspendUseCase {
    override suspend fun invoke(input: Unit): Result<List<Rocket>> {
        return asResult { repo.getRocketsSuspend().map { it.asRocket() } }
    }
}
