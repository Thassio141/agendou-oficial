package br.com.agendou.domain.repository

import java.time.LocalDateTime

data class ScheduleBookingRequest(
    val clientId: String,
    val professionalId: String,
    val serviceId: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime
)