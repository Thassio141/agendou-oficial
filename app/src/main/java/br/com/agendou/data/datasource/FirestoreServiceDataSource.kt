package br.com.agendou.data.datasource

import br.com.agendou.data.dto.ServiceDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreServiceDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val services = firestore.collection("services")

    suspend fun getServicesForProfessional(professionalId: String): List<ServiceDto> {
        return services
            .whereEqualTo("professionalId", professionalId)
            .whereEqualTo("deletedAt", null)
            .get()
            .await()
            .toObjects(ServiceDto::class.java)
    }

    suspend fun createService(service: ServiceDto): ServiceDto {
        services.document(service.id).set(service).await()
        return service
    }

    suspend fun getServiceById(id: String): ServiceDto? =
        services.document(id)
            .get()
            .await()
            .toObject(ServiceDto::class.java)

    suspend fun updateService(service: ServiceDto) {
        services.document(service.id).set(service).await()
    }
} 