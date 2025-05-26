package br.com.agendou.domain.usecases.user

import br.com.agendou.domain.model.User
import br.com.agendou.domain.repository.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userId: String): User? {
        return repository.getUser(userId)
    }
} 