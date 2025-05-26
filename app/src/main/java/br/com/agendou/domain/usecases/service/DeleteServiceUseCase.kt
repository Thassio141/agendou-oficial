package br.com.agendou.domain.usecases.service

import br.com.agendou.domain.repository.ServiceRepository
import javax.inject.Inject

class DeleteServiceUseCase @Inject constructor(
    private val repository: ServiceRepository
) {
    suspend operator fun invoke(serviceId: String) {
        repository.softDeleteService(serviceId)
    }
} 