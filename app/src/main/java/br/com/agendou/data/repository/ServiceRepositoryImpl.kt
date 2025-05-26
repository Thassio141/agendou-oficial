package br.com.agendou.data.repository

import br.com.agendou.data.datasource.FirestoreServiceDataSource
import br.com.agendou.data.dto.ServiceDto
import br.com.agendou.domain.model.Service
import br.com.agendou.domain.repository.ServiceRepository
import com.google.firebase.Timestamp
import java.util.UUID
import javax.inject.Inject

class ServiceRepositoryImpl @Inject constructor(
    private val dataSource: FirestoreServiceDataSource
) : ServiceRepository {

    override suspend fun getServicesForProfessional(professionalId: String): List<Service> {
        return dataSource.getServicesForProfessional(professionalId)
            .map { it.toDomain() }
    }

    override suspend fun createService(service: Service): Service {
        val serviceId = service.id.takeIf { it.isNotBlank() } ?: UUID.randomUUID().toString()
        
        val dto = ServiceDto(
            id = serviceId,
            name = service.name,
            description = service.description,
            duration = service.duration,
            price = service.price,
            professionalId = service.professionalId,
            deletedAt = null
        )
        
        dataSource.createService(dto)
        return dto.toDomain()
    }

    override suspend fun softDeleteService(serviceId: String) {
        val service = dataSource.getServiceById(serviceId) ?: return
        val updated = service.copy(
            deletedAt = Timestamp.now()
        )
        dataSource.updateService(updated)
    }
} 