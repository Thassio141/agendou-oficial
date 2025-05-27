package br.com.agendou.domain.usecases.auth

import br.com.agendou.domain.repository.AuthRepository
import javax.inject.Inject

class SendPasswordResetUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): Result<Unit> {
        return authRepository.sendPasswordReset(email)
    }
} 