package br.com.agendou.domain.usecases.user

import br.com.agendou.domain.repository.UserRepository
import javax.inject.Inject

class UpdatePhoneNumberUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userId: String, phoneNumber: String?) {
        repository.updatePhoneNumber(userId, phoneNumber)
    }
} 