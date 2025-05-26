package br.com.agendou.domain.repository

import br.com.agendou.domain.model.Service

interface ServiceRepository {
    suspend fun getServicesForProfessional(professionalId: String): List<Service>
    suspend fun createService(service: Service): Service
    suspend fun softDeleteService(serviceId: String): Unit
}