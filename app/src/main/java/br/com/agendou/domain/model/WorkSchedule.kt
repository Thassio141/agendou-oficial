package br.com.agendou.domain.model

data class WorkSchedule(
    val id: String,
    val professionalId: String,
    val dayOfWeek: Int,
    val startTime: String,
    val endTime: String,
    val granularityMin: Int? = null
)