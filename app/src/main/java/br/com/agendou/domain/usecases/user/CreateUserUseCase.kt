package br.com.agendou.domain.usecases.user

import br.com.agendou.domain.model.User
import br.com.agendou.domain.repository.UserRepository
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(
        user: User,
        phoneNumber: String? = null,
        profilePictureUrl: String? = null
    ): User {
        return repository.createUser(user, phoneNumber, profilePictureUrl)
    }
} 