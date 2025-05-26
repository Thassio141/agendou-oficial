package br.com.agendou.data.dto

import br.com.agendou.domain.enums.BookingStatus
import br.com.agendou.domain.model.Booking
import com.google.firebase.Timestamp

data class BookingDto(
    val id: String = "",
    val serviceId: String = "",
    val professionalId: String = "",
    val clientId: String = "",
    val startTime: Timestamp = Timestamp.now(),
    val endTime: Timestamp = Timestamp.now(),
    val status: String = "PENDING",
    val createdAt: Timestamp = Timestamp.now(),
    val deletedAt: Timestamp? = null
) {
    fun toDomain() = Booking(
        id = id,
        serviceId = serviceId,
        professionalId = professionalId,
        clientId = clientId,
        startTime = startTime,
        endTime = endTime,
        status = BookingStatus.valueOf(status),
        createdAt = createdAt,
        deletedAt = deletedAt
    )
}