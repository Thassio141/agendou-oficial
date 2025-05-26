package br.com.agendou.domain.usecases.service

import br.com.agendou.domain.model.Service
import br.com.agendou.domain.repository.ServiceRepository
import javax.inject.Inject

class GetServicesForProfessionalUseCase @Inject constructor(
    private val repository: ServiceRepository
) {
    suspend operator fun invoke(professionalId: String): List<Service> {
        return repository.getServicesForProfessional(professionalId)
    }
} 