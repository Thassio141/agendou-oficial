package br.com.agendou.data.dto

import br.com.agendou.domain.model.Service
import com.google.firebase.Timestamp

data class ServiceDto(
    val id: String = "",
    val professionalId: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val duration: Int = 0,
    val deletedAt: Timestamp? = null
) {
    fun toDomain() = Service(
        id = id,
        professionalId = professionalId,
        name = name,
        description = description,
        price = price,
        duration = duration,
        deletedAt = deletedAt
    )
}