package br.com.agendou.domain.usecases.user

import br.com.agendou.domain.model.User
import br.com.agendou.domain.repository.UserRepository
import javax.inject.Inject

class GetProfessionalsUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): List<User> {
        return userRepository.getProfessionals()
    }
} 