package br.com.agendou.domain.usecases.auth

import br.com.agendou.domain.enums.Role
import br.com.agendou.domain.repository.AuthRepository
import br.com.agendou.domain.repository.UserRepository
import br.com.agendou.domain.model.User
import com.google.firebase.Timestamp
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String, name: String): Result<String> =
        authRepository.signUp(email, password, name)
            .mapCatching { userId ->
                val now = Timestamp.now()
                val user = User(
                    id = userId,
                    name = name,
                    email = email,
                    role = Role.CLIENT,
                    phoneNumber = null,
                    profilePictureUrl = null,
                    createdAt = now
                )
                userRepository.createUser(user, null, null)
                userId
            }
} 