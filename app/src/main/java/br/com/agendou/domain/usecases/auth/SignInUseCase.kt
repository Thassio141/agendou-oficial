package br.com.agendou.domain.usecases.auth

import br.com.agendou.domain.repository.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<String> {
        return authRepository.signIn(email, password)
    }
} 