package br.com.agendou.domain.model

import br.com.agendou.domain.enums.BookingStatus
import com.google.firebase.Timestamp

data class Booking(
    val id: String,
    val clientId: String,
    val professionalId: String,
    val serviceId: String,
    val startTime: Timestamp,
    val endTime: Timestamp,
    val status: BookingStatus = BookingStatus.PENDING,
    val createdAt: Timestamp,
    val updatedAt: Timestamp? = null,
    val deletedAt: Timestamp? = null,
)