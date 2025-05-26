package br.com.agendou.domain.usecases.service

import br.com.agendou.domain.model.Service
import br.com.agendou.domain.repository.ServiceRepository
import javax.inject.Inject

class CreateServiceUseCase @Inject constructor(
    private val repository: ServiceRepository
) {
    suspend operator fun invoke(service: Service): Service {
        return repository.createService(service)
    }
} 